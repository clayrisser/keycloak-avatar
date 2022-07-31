/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/AvatarStorageProvider.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 04:53:06
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 06:21:20
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage;

import org.keycloak.provider.Provider;
import java.io.InputStream;

public interface AvatarStorageProvider extends Provider {
  void saveAvatarImage(String realmName, String userId, InputStream input, long inputSize);

  InputStream loadAvatarImage(String realmId, String userId);
}
