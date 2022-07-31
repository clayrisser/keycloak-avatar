/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Template.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Template.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:17:14
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 09:16:45
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class S3Template {
  private OkHttpClient httpClient;
  private final S3Config s3Config;
  private static final int TIMEOUT_SECONDS = 15;

  public S3Template(S3Config s3Config) {
    this.s3Config = s3Config;
    httpClient = new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build();
  }

  public <T> T execute(S3Callback<T> callback) {
    try {
      MinioClient minioClient = MinioClient.builder().endpoint(s3Config.endpoint).region(s3Config.region)
          .credentials(s3Config.accessKey, s3Config.secretKey)
          .httpClient(httpClient).build();
      return callback.doInMinio(minioClient);
    } catch (Exception mex) {
      throw new RuntimeException(mex);
    }
  }

  public void ensureBucketExists(String bucketName) {
    execute(minioClient -> {
      boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!exists) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
      return null;
    });
  }

  public String getBucketName() {
    return this.s3Config.bucket;
  }

  public String getObjectName(String objectName) {
    if (this.s3Config.prefix.length() > 0) {
      return this.s3Config.prefix + "/" + objectName;
    }
    return objectName;
  }
}
