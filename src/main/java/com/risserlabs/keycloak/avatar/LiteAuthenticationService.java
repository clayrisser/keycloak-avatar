/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/LiteAuthenticationService.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 04-08-2022 13:44:07
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import javax.ws.rs.core.HttpHeaders;

import org.jboss.logging.Logger;
import org.keycloak.TokenVerifier;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.common.constants.ServiceAccountConstants;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.oidc.TokenManager.NotBeforeCheck;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.Urls;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.UserSessionCrossDCManager;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.sessions.RootAuthenticationSessionModel;

public class LiteAuthenticationService {
  private KeycloakSession session;
  private AppAuthManager appAuthManager;
  private Logger logger = Logger.getLogger(LiteAuthenticationService.class);

  LiteAuthenticationService(KeycloakSession session) {
    this.session = session;
    this.appAuthManager = new AppAuthManager();
  }

  public AuthResult resolveAuthentication(HttpHeaders headers, ClientConnection clientConnection) {
    RealmModel realm = session.getContext().getRealm();
    AuthResult authResult = this.appAuthManager.authenticateIdentityCookie(session,
        realm);
    if (authResult != null) {
      return authResult;
    }
    String accessToken = AppAuthManager.extractAuthorizationHeaderTokenOrReturnNull(headers);
    if (accessToken == null) {
      return null;
    }
    AccessToken token;
    ClientModel client;
    try {
      TokenVerifier<AccessToken> verifier = TokenVerifier.create(accessToken, AccessToken.class).withDefaultChecks()
          .realmUrl(Urls.realmIssuer(session.getContext().getUri().getBaseUri(), realm.getName()));
      SignatureVerifierContext verifierContext = session
          .getProvider(SignatureProvider.class, verifier.getHeader().getAlgorithm().name())
          .verifier(verifier.getHeader().getKeyId());
      verifier.verifierContext(verifierContext);
      token = verifier.verify().getToken();
      if (token == null) {
        return null;
      }
      try {
        client = realm.getClientByClientId(token.getIssuedFor());
      } catch (Exception e) {
        logger.error(e);
        return null;
      }
      if (client == null) {
        return null;
      }
      TokenVerifier.createWithoutSignature(token)
          .withChecks(NotBeforeCheck.forModel(client), new TokenManager.TokenRevocationCheck(session))
          .verify();
    } catch (VerificationException e) {
      return null;
    }
    session.getContext().setClient(client);
    if (!client.isEnabled()) {
      return null;
    }
    UserSessionModel userSession = findValidSession(token, client, clientConnection);
    if (userSession == null) {
      return null;
    }
    UserModel user = userSession.getUser();
    if (user == null) {
      return null;
    }
    return new AuthResult(user, userSession, token, client);
  }

  private UserSessionModel findValidSession(AccessToken token, ClientModel clientModel,
      ClientConnection clientConnection) {
    RealmModel realm = session.getContext().getRealm();
    if (token.getSessionState() == null) {
      return createTransientSessionForClient(token, clientModel, clientConnection);
    }
    UserSessionModel userSession = new UserSessionCrossDCManager(session).getUserSessionWithClient(realm,
        token.getSessionState(), false, clientModel.getId());
    UserSessionModel offlineUserSession = null;
    if (AuthenticationManager.isSessionValid(realm, userSession)) {
      if (!checkTokenIssuedAt(token, userSession, clientModel)) {
        return null;
      }
      return userSession;
    } else {
      offlineUserSession = new UserSessionCrossDCManager(session).getUserSessionWithClient(realm,
          token.getSessionState(), true, clientModel.getId());
      if (AuthenticationManager.isOfflineSessionValid(realm, offlineUserSession)) {
        if (!checkTokenIssuedAt(token, offlineUserSession, clientModel)) {
          return null;
        }
        return offlineUserSession;
      }
    }
    return null;
  }

  private UserSessionModel createTransientSessionForClient(AccessToken token, ClientModel client,
      ClientConnection clientConnection) {
    RealmModel realm = session.getContext().getRealm();
    UserModel user = TokenManager.lookupUserFromStatelessToken(session, realm, token);
    if (user == null) {
      return null;
    }
    UserSessionModel userSession = session.sessions().createUserSession(KeycloakModelUtils.generateId(), realm, user,
        user.getUsername(), clientConnection.getRemoteAddr(),
        ServiceAccountConstants.CLIENT_AUTH, false, null, null, UserSessionModel.SessionPersistenceState.TRANSIENT);
    RootAuthenticationSessionModel rootAuthSession = session.authenticationSessions()
        .createRootAuthenticationSession(realm);
    AuthenticationSessionModel authSession = rootAuthSession.createAuthenticationSession(client);
    authSession.setAuthenticatedUser(userSession.getUser());
    authSession.setProtocol(OIDCLoginProtocol.LOGIN_PROTOCOL);
    authSession.setClientNote(OIDCLoginProtocol.ISSUER,
        Urls.realmIssuer(session.getContext().getUri().getBaseUri(), realm.getName()));
    AuthenticationManager.setClientScopesInSession(authSession);
    TokenManager.attachAuthenticationSession(session, userSession, authSession);
    return userSession;
  }

  private boolean checkTokenIssuedAt(AccessToken token, UserSessionModel userSession, ClientModel client) {
    if (token.isIssuedBeforeSessionStart(userSession.getStarted())) {
      return false;
    }
    AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(client.getId());
    if (token.isIssuedBeforeSessionStart(clientSession.getStarted())) {
      return false;
    }
    return true;
  }
}
