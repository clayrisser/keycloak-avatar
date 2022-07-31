/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AvatarResourceProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/AvatarResourceProvider.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 30-07-2022 11:59:49
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 15:08:23
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class AvatarResourceProvider implements RealmResourceProvider {

  private final KeycloakSession keycloakSession;

  public AvatarResourceProvider(KeycloakSession keycloakSession) {
    this.keycloakSession = keycloakSession;
  }

  @Override
  public Object getResource() {
    return new AvatarResource(keycloakSession);
  }

  @Override
  public void close() {
    // NOOP
  }
}
