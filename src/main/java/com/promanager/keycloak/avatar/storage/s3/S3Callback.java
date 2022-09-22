/**
 * File: /src/main/java/com/promanager/keycloak/avatar/storage/s3/S3Callback.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 31-07-2022 05:48:28
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:47
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar.storage.s3;

import io.minio.MinioClient;

public interface S3Callback<T> {
  T doInMinio(MinioClient minioClient) throws Exception;
}
