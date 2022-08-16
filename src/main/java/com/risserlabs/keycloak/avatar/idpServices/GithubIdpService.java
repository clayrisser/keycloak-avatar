/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/idpServices/GithubIdpService.java
 * /src/main/java/com/risserlabs/keycloak/avatar/idpServices/GithubIdpService.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 07-08-2022 05:11:04
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.idpServices;

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
