/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 30-07-2022 12:03:15
 * Author: Clay Risser
 * -----
 * Last Modified: 04-08-2022 14:57:56
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.json.Json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resources.Cors;

public abstract class AbstractAvatarResource {
  protected static final String AVATAR_IMAGE_PARAMETER = "image";

  protected KeycloakSession session;

  public AbstractAvatarResource(KeycloakSession session) {
    this.session = session;
  }

  protected AvatarStorageProvider lookupAvatarStorageProvider(KeycloakSession keycloakSession) {
    return keycloakSession.getProvider(AvatarStorageProvider.class);
  }

  public AvatarStorageProvider getAvatarStorageProvider() {
    return lookupAvatarStorageProvider(session);
  }

  protected Response badRequest() {
    return badRequest("", null);
  }

  protected Response badRequest(String errorMessage) {
    return badRequest(errorMessage, null);
  }

  protected Response badRequest(Cors cors) {
    return badRequest("", cors);
  }

  protected Response badRequest(String errorMessage, Cors cors) {
    String body = "";
    String mediaType = MediaType.TEXT_PLAIN;
    if (errorMessage != null && errorMessage.length() > 0) {
      body = Json.createObjectBuilder()
          .add("error", errorMessage).build().toString();
      mediaType = MediaType.APPLICATION_JSON;
    }
    ResponseBuilder responseBuilder = Response
        .ok(body, mediaType)
        .status(Response.Status.BAD_REQUEST);
    if (cors != null) {
      return cors.builder(responseBuilder).build();
    }
    return responseBuilder.build();
  }

  protected Response unauthorized() {
    return unauthorized(null);
  }

  protected Response unauthorized(Cors cors) {
    ResponseBuilder responseBuilder = Response
        .ok("", MediaType.TEXT_PLAIN)
        .status(Response.Status.UNAUTHORIZED);
    if (cors != null) {
      return cors.builder(responseBuilder).build();
    }
    return responseBuilder.build();
  }

  protected void uploadAvatarImage(String realmName, String userId, InputStream imageInputStream) {
    getAvatarStorageProvider().uploadAvatarImage(realmName, userId, imageInputStream);
  }

  protected StreamingOutput downloadAvatarImage(String realmId, String userId) {
    return (output) -> copyStream(
        getAvatarStorageProvider().downloadAvatarImage(realmId, userId),
        output);
  }

  private void copyStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[16384];
    int len;
    while ((len = in.read(buffer)) != -1) {
      out.write(buffer, 0, len);
    }
    out.flush();
  }
}
