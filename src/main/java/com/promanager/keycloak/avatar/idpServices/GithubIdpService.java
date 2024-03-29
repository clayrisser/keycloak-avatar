/**
 * File: /src/main/java/com/promanager/keycloak/avatar/idpServices/GithubIdpService.java
 * /src/main/java/com/promanager/keycloak/avatar/idpServices/GithubIdpService.java
 * Project: @promanager/keycloak-avatar-client
 * File Created: 07-08-2022 05:11:04
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 11:00:50
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

package com.promanager.keycloak.avatar.idpServices;

public class GithubIdpService extends IdpService {
  private String apiBaseUrl = "https://api.github.com";

  @Override
  protected String getUserEndpoint() {
    return apiBaseUrl + "/user";
  }

  @Override
  public String getAvatarUrl(String idpToken) {
    return getAvatarUrl("avatar_url", idpToken);
  }
}
