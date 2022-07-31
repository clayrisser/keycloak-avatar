/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AvatarResource.java
 * Project: keycloak-account-avatar
 * File Created: 30-07-2022 12:02:44
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 07:06:42
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import java.io.InputStream;
import java.util.Objects;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resources.RealmsResource;

public class AvatarResource extends AbstractAvatarResource {
  Logger logger = Logger.getLogger(AvatarResource.class);

  public static final String STATE_CHECKER_ATTRIBUTE = "state_checker";
  public static final String STATE_CHECKER_PARAMETER = "stateChecker";

  private final AuthenticationManager.AuthResult auth;

  public AvatarResource(KeycloakSession session) {
    super(session);
    this.auth = resolveAuthentication(session);
  }

  private AuthenticationManager.AuthResult resolveAuthentication(KeycloakSession keycloakSession) {
    AppAuthManager appAuthManager = new AppAuthManager();
    RealmModel realm = keycloakSession.getContext().getRealm();
    AuthenticationManager.AuthResult authResult = appAuthManager.authenticateIdentityCookie(keycloakSession, realm);
    if (authResult != null) {
      return authResult;
    }
    return null;
  }

  @GET
  @NoCache
  @Produces({ "image/png", "image/jpeg", "image/gif" })
  public Response getCurrentUserAvatarImage() {
    if (auth == null) {
      return unauthorized();
    }
    String realmName = auth.getSession().getRealm().getName();
    String userId = auth.getUser().getId();
    return Response.ok(fetchUserImage(realmName, userId)).build();
  }

  @POST
  @NoCache
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadCurrentUserAvatarImage(MultipartFormDataInput input, @Context UriInfo uriInfo) {
    if (auth == null) {
      return unauthorized();
    }
    if (!isValidStateChecker(input)) {
      return badRequest();
    }
    try {
      InputStream imageInputStream = input.getFormDataPart(AVATAR_IMAGE_PARAMETER, InputStream.class, null);
      String realmName = auth.getSession().getRealm().getName();
      String userId = auth.getUser().getId();
      // TODO: get stream size
      saveUserImage(realmName, userId, imageInputStream, -1);
      if (uriInfo.getQueryParameters().containsKey("account")) {
        return Response
            .seeOther(RealmsResource.accountUrl(session.getContext().getUri().getBaseUriBuilder()).build(realmName))
            .build();
      }
      return Response.ok().build();
    } catch (Exception ex) {
      return Response.serverError().build();
    }
  }

  private boolean isValidStateChecker(MultipartFormDataInput input) {
    try {
      String actualStateChecker = input.getFormDataPart(STATE_CHECKER_PARAMETER, String.class, null);
      String requiredStateChecker = (String) session.getAttribute(STATE_CHECKER_ATTRIBUTE);
      return Objects.equals(requiredStateChecker, actualStateChecker);
    } catch (Exception ex) {
      return false;
    }
  }
}
