/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AvatarResource.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 30-07-2022 12:02:44
 * Author: Clay Risser
 * -----
 * Last Modified: 06-08-2022 15:28:57
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.resources.Cors;
import org.keycloak.services.resources.RealmsResource;

public class AvatarResource extends AbstractAvatarResource {
  private Logger logger = Logger.getLogger(AvatarResource.class);
  private final LiteAuthenticationService liteAuthenticationService;

  public static final String STATE_CHECKER_ATTRIBUTE = "state_checker";
  public static final String STATE_CHECKER_PARAMETER = "stateChecker";

  public AvatarResource(KeycloakSession session) {
    super(session);
    this.liteAuthenticationService = new LiteAuthenticationService(session);
  }

  @OPTIONS
  public Response optionsCurrentUserAvatarImage(@Context HttpRequest request) {
    return Cors.add(request, Response.ok("", MediaType.TEXT_PLAIN)).auth().preflight().build();
  }

  @GET
  @Produces({ "image/png", "image/jpeg", "image/gif" })
  public Response getCurrentUserAvatarImage(
      @Context HttpRequest request,
      @Context HttpHeaders headers,
      @Context ClientConnection clientConnection) {
    Cors cors = Cors.add(request).auth().allowedMethods(request.getHttpMethod()).auth()
        .exposedHeaders(Cors.ACCESS_CONTROL_ALLOW_METHODS);
    AuthResult authResult = liteAuthenticationService.resolveAuthentication(headers, clientConnection);
    if (authResult == null) {
      cors.allowAllOrigins();
      return unauthorized(cors);
    }
    testFederated(authResult);
    String realmName = session.getContext().getRealm().getName();
    String userId = authResult.getSession().getUser().getId();
    StreamingOutput imageStream = downloadAvatarImage(realmName, userId);
    cors.allowedOrigins(session, authResult.getClient());
    return Response.ok(imageStream, "image/png").build();
  }

  @POST
  @NoCache
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response postCurrentUserAvatarImage(
      MultipartFormDataInput input,
      @Context HttpRequest request,
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @Context ClientConnection clientConnection) {
    RealmModel realm = session.getContext().getRealm();
    Cors cors = Cors.add(request).auth().allowedMethods(request.getHttpMethod()).auth()
        .exposedHeaders(Cors.ACCESS_CONTROL_ALLOW_METHODS);
    AuthResult authResult = liteAuthenticationService.resolveAuthentication(headers, clientConnection);
    if (authResult == null) {
      cors.allowAllOrigins();
      return unauthorized(cors);
    }
    try {
      InputStream imageInputStream = input.getFormDataPart(AVATAR_IMAGE_PARAMETER, InputStream.class, null);
      String realmName = realm.getName();
      String userId = authResult.getSession().getUser().getId();
      uploadAvatarImage(realmName, userId, imageInputStream);
      if (uriInfo.getQueryParameters().containsKey("account")) {
        return Response
            .seeOther(RealmsResource.accountUrl(session.getContext().getUri().getBaseUriBuilder()).build(realmName))
            .build();
      }
      cors.allowedOrigins(session, authResult.getClient());
      return cors.builder(Response.ok("", MediaType.TEXT_PLAIN)).build();
    } catch (Exception ex) {
      logger.error(ex);
      cors.allowedOrigins(session, authResult.getClient());
      return cors.builder(Response.ok("", MediaType.TEXT_PLAIN).status(500)).build();
    }
  }

  @OPTIONS
  @Path("{userId}")
  public Response optionsUserAvatarImage(@Context HttpRequest request) {
    return Cors.add(request, Response.ok("", MediaType.TEXT_PLAIN)).auth().preflight().build();
  }

  @GET
  @Path("{userId}")
  @Produces({ "image/png", "image/jpeg", "image/gif" })
  public Response getUserAvatarImage(
      @Context HttpRequest request,
      @PathParam("userId") String userId,
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers) {
    Cors cors = Cors.add(request).auth().allowedMethods(request.getHttpMethod()).auth()
        .exposedHeaders(Cors.ACCESS_CONTROL_ALLOW_METHODS);
    String realmName = getRealmName(uriInfo);
    StreamingOutput imageStream = downloadAvatarImage(realmName, userId);
    cors.allowAllOrigins();
    return cors.builder(Response.ok(imageStream, "image/png")).build();
  }

  private String getRealmName(UriInfo uriInfo) {
    String realmName = session.getContext().getRealm().getName();
    if (realmName != null) {
      return realmName;
    }
    realmName = "master";
    Pattern pattern = Pattern.compile("/realms/[^/]+", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(uriInfo.getPath());
    if (matcher.find()) {
      realmName = matcher.group(0).substring(8);
    }
    return realmName;
  }

  private void testFederated(AuthResult authResult) {
    UserModel user = authResult.getUser();
    RealmModel realm = session.getContext().getRealm();
    if (user == null) {
      logger.info("NO USER");
      return;
    }
    ArrayList<FederatedIdentityModel> federatedIdentities = new ArrayList<FederatedIdentityModel>(
        session.users().getFederatedIdentitiesStream(realm, user).collect(Collectors.toList()));
    logger.info(federatedIdentities.size());
    for (int i = 0; i < federatedIdentities.size(); i++) {
      FederatedIdentityModel identityProvider = federatedIdentities.get(i);
      logger.info(identityProvider.getIdentityProvider());
      logger.info(identityProvider.getUserId());
      logger.info(identityProvider.getUserName());
      logger.info(identityProvider.getToken());
    }
  }
}
