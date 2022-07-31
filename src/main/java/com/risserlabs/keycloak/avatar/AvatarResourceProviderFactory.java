/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AvatarResourceProviderFactory.java
 * Project: keycloak-account-avatar
 * File Created: 30-07-2022 11:40:29
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 09:16:52
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

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
