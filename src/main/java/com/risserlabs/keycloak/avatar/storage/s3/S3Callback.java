/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/storage/s3/S3Callback.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 31-07-2022 05:48:28
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.storage.s3;

import io.minio.MinioClient;

public interface S3Callback<T> {
  T doInMinio(MinioClient minioClient) throws Exception;
}
