/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AvatarUtil.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 07-08-2022 07:28:11
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AvatarUtil {
  public static void copyStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[16384];
    int len;
    while ((len = in.read(buffer)) != -1) {
      out.write(buffer, 0, len);
    }
    out.flush();
  }
}
