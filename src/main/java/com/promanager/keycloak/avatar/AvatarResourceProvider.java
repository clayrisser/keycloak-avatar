/**
 * File: /src/main/java/com/promanager/keycloak/avatar/AvatarResourceProvider.java
 * /src/main/java/com/promanager/keycloak/avatar/AvatarResourceProvider.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 30-07-2022 11:59:49
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:53
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar;

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
  }
}
