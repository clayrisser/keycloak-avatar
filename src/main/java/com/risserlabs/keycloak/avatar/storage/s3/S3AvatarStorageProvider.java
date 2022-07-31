/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3AvatarStorageProvider.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 05:15:04
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 15:08:23
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
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
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
            return getSinglePixelImage();
          }
        });
  }

  @Override
  public void close() {
    // NOOP
  }

  private InputStream getSinglePixelImage() {
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Color black = new Color(0, 0, 0);
    image.setRGB(0, 0, black.getRGB());
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", output);
    } catch (IOException ex) {
      logger.error(ex);
    }
    ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
    return input;
  }
}
