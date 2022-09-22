/**
 * File: /src/main/java/com/promanager/keycloak/avatar/AvatarUtil.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 07-08-2022 07:28:11
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:52
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar;

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
