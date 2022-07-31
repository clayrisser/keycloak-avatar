/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Config.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:09:58
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 07:16:23
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

public class S3Config {
  public final String accessKey;
  public final String bucketName;
  public final String prefix;
  public final String region;
  public final String secretKey;
  public final String endpoint;

  S3Config(String endpoint, String region, String accessKey, String secretKey, String bucketName, String prefix) {
    this.accessKey = accessKey;
    this.bucketName = bucketName;
    this.endpoint = endpoint;
    this.prefix = prefix;
    this.region = region;
    this.secretKey = secretKey;
  }
}
