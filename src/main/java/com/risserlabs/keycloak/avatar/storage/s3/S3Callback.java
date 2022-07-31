/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Callback.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 05:48:28
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 15:08:23
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import io.minio.MinioClient;

public interface S3Callback<T> {
  T doInMinio(MinioClient minioClient) throws Exception;
}
