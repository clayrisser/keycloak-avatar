/**
 * File: /src/main/java/com/promanager/keycloak/avatar/storage/AvatarStorageProviderSpi.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 31-07-2022 05:04:18
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:49
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar.storage;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class AvatarStorageProviderSpi implements Spi {
  @Override
  public boolean isInternal() {
    return false;
  }

  @Override
  public String getName() {
    return "avatar-storage";
  }

  @Override
  public Class<? extends Provider> getProviderClass() {
    return AvatarStorageProvider.class;
  }

  @Override
  public Class<? extends ProviderFactory<?>> getProviderFactoryClass() {
    return AvatarStorageProviderFactory.class;
  }
}
