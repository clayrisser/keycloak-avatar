/**
 * File: /src/main/java/com/risserlabs/keycloak/authenticators/browser/AccountAvatarAuthenticatorFactory.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 02-08-2022 12:19:21
 * Author: Clay Risser
 * -----
 * Last Modified: 02-08-2022 12:35:30
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.authenticators.browser;

import java.util.Arrays;
import java.util.List;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class AccountAvatarAuthenticatorFactory implements AuthenticatorFactory {
  public static final String ID = "account_avatar_authenticator";

  private static final Authenticator AUTHENTICATOR_INSTANCE = new AccountAvatarAuthenticator();

  @Override
  public Authenticator create(KeycloakSession keycloakSession) {
    return AUTHENTICATOR_INSTANCE;
  }

  @Override
  public String getDisplayType() {
    return "Account Avatar Authenticator";
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return new AuthenticationExecutionModel.Requirement[] {
        AuthenticationExecutionModel.Requirement.REQUIRED,
        AuthenticationExecutionModel.Requirement.ALTERNATIVE,
        AuthenticationExecutionModel.Requirement.DISABLED,
    };
  }

  @Override
  public boolean isUserSetupAllowed() {
    return false;
  }

  @Override
  public String getHelpText() {
    return "";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return Arrays.asList();
  }

  @Override
  public String getReferenceCategory() {
    return null;
  }

  @Override
  public void init(Config.Scope scope) {
  }

  @Override
  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
  }

  @Override
  public void close() {
  }

  @Override
  public String getId() {
    return AccountAvatarAuthenticatorFactory.ID;
  }
}
