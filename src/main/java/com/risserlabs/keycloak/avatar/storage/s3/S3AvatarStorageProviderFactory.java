/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProviderFactory.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProviderFactory.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 31-07-2022 05:06:57
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 12:14:54
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import com.risserlabs.keycloak.avatar.storage.AvatarStorageProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class S3AvatarStorageProviderFactory implements AvatarStorageProviderFactory {
  private static final String DEFAULT_ENDPOINT = "http://localhost:9000";
  private static final String DEFAULT_ACCESS_KEY = "";
  private static final String DEFAULT_SECRET_KEY = "";
  private static final String DEFAULT_BUCKET_NAME = "avatar";
  private static final String DEFAULT_REGION = "us-east-1";
  private static final String DEFAULT_PREFIX = "";
  private S3Config s3Config;

  @Override
  public AvatarStorageProvider create(KeycloakSession session) {
    return new S3AvatarStorageProvider(s3Config);
  }

  @Override
  public void init(Config.Scope config) {
    String endpoint = config.get("server-url", get(System.getenv("ACCOUNT_AVATAR_S3_ENDPOINT"), DEFAULT_ENDPOINT));
    String accessKey = config.get("access-key", get(System.getenv("ACCOUNT_AVATAR_S3_ACCESS_KEY"), DEFAULT_ACCESS_KEY));
    String secretKey = config.get("secret-key", get(System.getenv("ACCOUNT_AVATAR_S3_SECRET_KEY"), DEFAULT_SECRET_KEY));
    String bucketName = config.get("bucket-name",
        get(System.getenv("ACCOUNT_AVATAR_S3_BUCKET_NAME"), DEFAULT_BUCKET_NAME));
    String prefix = config.get("prefix", get(System.getenv("ACCOUNT_AVATAR_S3_PREFIX"), DEFAULT_PREFIX));
    String region = config.get("prefix", get(System.getenv("ACCOUNT_AVATAR_S3_REGION"), DEFAULT_REGION));
    this.s3Config = new S3Config(endpoint, region, accessKey, secretKey, bucketName, prefix);
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

  private String get(String a, String b) {
    if (a == null || a.isEmpty()) {
      return b;
    }
    return a;
  }
}
