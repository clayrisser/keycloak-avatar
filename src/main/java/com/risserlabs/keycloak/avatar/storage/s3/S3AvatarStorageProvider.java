/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 05:15:04
 * Author: Clay Risser
 * -----
 * Last Modified: 07-08-2022 04:40:21
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import io.minio.ErrorCode;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import java.io.InputStream;
import org.jboss.logging.Logger;

public class S3AvatarStorageProvider implements AvatarStorageProvider {
  private Logger logger = Logger.getLogger(S3AvatarStorageProvider.class);
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
      minioClient.putObject(
          PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
              input, -1, PART_SIZE).contentType("image/png")
              .build());
      return null;
    });
  }

  @Override
  public InputStream downloadAvatarImage(String realmName, String userId) {
    String bucketName = s3Template.getBucketName();
    String objectName = s3Template.getObjectName(realmName + "/" + userId);
    return s3Template.execute(
        (minioClient) -> {
          try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
          } catch (ErrorResponseException ex) {
            ErrorResponse errorResponse = ex.errorResponse();
            ErrorCode errorCode = null;
            if (errorResponse != null) {
              errorCode = errorResponse.errorCode();
            }
            if (errorCode == null || (errorCode != ErrorCode.NO_SUCH_BUCKET && errorCode != ErrorCode.NO_SUCH_OBJECT)) {
              logger.error(ex);
            }
            return null;
          }
        });
  }

  @Override
  public void close() {
  }
}
