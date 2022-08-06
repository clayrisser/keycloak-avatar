/**
 * File: /src/main/java/com/risserlabs/keycloak/authenticators/browser/AccountAvatarAuthenticator.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 02-08-2022 12:31:31
 * Author: Clay Risser
 * -----
 * Last Modified: 06-08-2022 14:58:31
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.authenticators.browser;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class AccountAvatarAuthenticator implements Authenticator {
  private Logger logger = Logger.getLogger(AccountAvatarAuthenticator.class);

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    UserModel user = context.getUser();
    RealmModel realm = context.getRealm();
    if (user == null) {
      context.failure(AuthenticationFlowError.UNKNOWN_USER);
      return;
    }
    ArrayList<FederatedIdentityModel> federatedIdentities = new ArrayList<FederatedIdentityModel>(
        context.getSession().users().getFederatedIdentitiesStream(realm, user).collect(Collectors.toList()));
    logger.info(federatedIdentities.size());
    for (int i = 0; i < federatedIdentities.size(); i++) {
      FederatedIdentityModel identityProvider = federatedIdentities.get(i);
      logger.info(identityProvider.getIdentityProvider());
      logger.info(identityProvider.getUserId());
      logger.info(identityProvider.getUserName());
      logger.info(identityProvider.getToken());
    }
    context.success();
  }

  @Override
  public void action(AuthenticationFlowContext context) {
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
  }

  @Override
  public void close() {
  }
}
