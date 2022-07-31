/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProviderFactory.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProviderFactory.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:06:57
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 07:14:38
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import com.risserlabs.keycloak.avatar.storage.AvatarStorageProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class S3AvatarStorageProviderFactory implements AvatarStorageProviderFactory {
  private static final String DEFAULT_SERVER_URL = "http://172.17.0.2:9000";
  private static final String DEFAULT_ACCESS_KEY = "J6zZDOKyRIskxQem";
  private static final String DEFAULT_SECRET_KEY = "CSWnjx9dkyoTLiWSG6nWAGcZZq6x8Rrl";
  private static final String DEFAULT_BUCKET_NAME = "abc123";
  private static final String DEFAULT_REGION = "us-east-1";
  private static final String DEFAULT_PREFIX = "ppp";
  private S3Config s3Config;

  @Override
  public AvatarStorageProvider create(KeycloakSession session) {
    return new S3AvatarStorageProvider(s3Config);
  }

  @Override
  public void init(Config.Scope config) {
    String serverUrl = config.get("server-url", DEFAULT_SERVER_URL);
    String accessKey = config.get("access-key", DEFAULT_ACCESS_KEY);
    String secretKey = config.get("secret-key", DEFAULT_SECRET_KEY);
    String bucketName = config.get("bucket-name", DEFAULT_BUCKET_NAME);
    String prefix = config.get("prefix", DEFAULT_PREFIX);
    String region = config.get("prefix", DEFAULT_REGION);
    this.s3Config = new S3Config(serverUrl, region, accessKey, secretKey, bucketName, prefix);
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    // NOOP
  }

  @Override
  public void close() {
    // NOOP
  }

  @Override
  public String getId() {
    return "avatar-storage-minio";
  }
}
