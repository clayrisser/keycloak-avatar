/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:15:04
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 06:51:46
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.InputStream;

public class S3AvatarStorageProvider implements AvatarStorageProvider {
  private final S3Template s3Template;

  public S3AvatarStorageProvider(S3Config s3Config) {
    this.s3Template = new S3Template(s3Config);
  }

  @Override
  public void saveAvatarImage(String realmName, String userId, InputStream input, long inputSize) {
    String bucketName = s3Template.getBucketName();
    String objectName = s3Template.getObjectName(realmName + "/" + userId);
    s3Template.ensureBucketExists(bucketName);
    s3Template.execute((MinioClient minioClient) -> {
      minioClient.putObject(
          PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
              input, inputSize, -1).contentType("image/png")
              .build());
      return null;
    });
  }

  @Override
  public InputStream loadAvatarImage(String realmName, String userId) {
    String bucketName = s3Template.getBucketName();
    String objectName = s3Template.getObjectName(realmName + "/" + userId);
    return s3Template.execute(
        minioClient -> minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build()));
  }

  @Override
  public void close() {
    // NOOP
  }
}
