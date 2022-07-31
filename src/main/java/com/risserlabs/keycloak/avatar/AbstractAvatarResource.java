/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * Project: keycloak-account-avatar
 * File Created: 30-07-2022 12:03:15
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 06:59:27
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.keycloak.models.KeycloakSession;

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
    return badRequest("bad request");
  }

  protected Response badRequest(String body) {
    return Response.ok(body, MediaType.TEXT_PLAIN).status(Response.Status.BAD_REQUEST).build();
  }

  protected Response unauthorized() {
    return Response.ok("unauthorized", MediaType.TEXT_PLAIN).status(Response.Status.UNAUTHORIZED).build();
  }

  protected void saveUserImage(String realmName, String userId, InputStream imageInputStream, long imageInputSize) {
    getAvatarStorageProvider().saveAvatarImage(realmName, userId, imageInputStream, imageInputSize);
  }

  protected StreamingOutput fetchUserImage(String realmId, String userId) {
    return output -> copyStream(getAvatarStorageProvider().loadAvatarImage(realmId, userId), output);
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
