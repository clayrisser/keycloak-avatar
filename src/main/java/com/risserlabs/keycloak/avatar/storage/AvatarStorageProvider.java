/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/AvatarStorageProvider.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 31-07-2022 04:53:06
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage;

import org.keycloak.provider.Provider;
import java.io.InputStream;

public interface AvatarStorageProvider extends Provider {
  void uploadAvatarImage(String realmName, String userId, InputStream input);

  InputStream downloadAvatarImage(String realmId, String userId);
}
