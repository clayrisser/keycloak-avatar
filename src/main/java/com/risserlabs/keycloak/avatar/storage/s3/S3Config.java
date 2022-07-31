/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Config.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:09:58
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 07:36:02
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

public class S3Config {
  public final String accessKey;
  public final String bucket;
  public final String prefix;
  public final String region;
  public final String secretKey;
  public final String endpoint;

  S3Config(String endpoint, String region, String accessKey, String secretKey, String bucket, String prefix) {
    this.accessKey = accessKey;
    this.bucket = bucket;
    this.endpoint = endpoint;
    this.prefix = prefix;
    this.region = region;
    this.secretKey = secretKey;
  }
}
