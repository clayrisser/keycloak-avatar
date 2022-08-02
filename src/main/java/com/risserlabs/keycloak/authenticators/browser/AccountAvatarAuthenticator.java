/**
 * File: /src/main/java/com/risserlabs/keycloak/authenticators/browser/AccountAvatarAuthenticator.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 02-08-2022 12:31:31
 * Author: Clay Risser
 * -----
 * Last Modified: 02-08-2022 12:38:24
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.authenticators.browser;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class AccountAvatarAuthenticator implements Authenticator {
  @Override
  public void authenticate(AuthenticationFlowContext context) {
    context.success();
  }

  @Override
  public void action(AuthenticationFlowContext context) {
    context.success();
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
  }

  @Override
  public void close() {
  }
}
