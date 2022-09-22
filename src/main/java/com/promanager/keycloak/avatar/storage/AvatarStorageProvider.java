/**
 * File: /src/main/java/com/promanager/keycloak/avatar/storage/AvatarStorageProvider.java
 * /src/main/java/com/promanager/keycloak/avatar/storage/AvatarStorageProvider.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 31-07-2022 04:53:06
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:48
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar.storage;

import org.keycloak.provider.Provider;
import java.io.InputStream;

public interface AvatarStorageProvider extends Provider {
  void uploadAvatarImage(String realmName, String userId, InputStream input);

  InputStream downloadAvatarImage(String realmId, String userId);
}
