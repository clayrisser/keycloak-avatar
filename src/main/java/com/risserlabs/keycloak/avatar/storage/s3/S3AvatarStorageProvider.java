/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * Project: keycloak-account-avatar
 * File Created: 31-07-2022 05:15:04
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 09:10:17
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
  private static long PART_SIZE = 8388608;
  private final S3Template s3Template;

  public S3AvatarStorageProvider(S3Config s3Config) {
    this.s3Template = new S3Template(s3Config);
  }

  @Override
  public void uploadAvatarImage(String realmName, String userId, InputStream input) {
    String bucketName = s3Template.getBucketName();
    String objectName = s3Template.getObjectName(realmName + "/" + userId);
    s3Template.ensureBucketExists(bucketName);
    s3Template.execute((MinioClient minioClient) -> {
      PutObjectArgs bbb = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
          input, -1, PART_SIZE).contentType("image/png")
          .build();
      minioClient.putObject(bbb);
      return null;
    });
  }

  @Override
  public InputStream downloadAvatarImage(String realmName, String userId) {
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
