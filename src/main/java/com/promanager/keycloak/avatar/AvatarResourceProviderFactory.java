/**
 * File: /src/main/java/com/promanager/keycloak/avatar/AvatarResourceProviderFactory.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 30-07-2022 11:40:29
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:52
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;
import static org.keycloak.Config.Scope;

public class AvatarResourceProviderFactory implements RealmResourceProviderFactory {

  private AvatarResourceProvider avatarResourceProvider;

  @Override
  public RealmResourceProvider create(KeycloakSession keycloakSession) {
    if (avatarResourceProvider == null) {
      avatarResourceProvider = new AvatarResourceProvider(keycloakSession);
    }
    return avatarResourceProvider;
  }

  @Override
  public void init(Scope scope) {
  }

  @Override
  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

  }

  @Override
  public void close() {
    // NOOP
  }

  @Override
  public String getId() {
    return "avatar";
  }
}
