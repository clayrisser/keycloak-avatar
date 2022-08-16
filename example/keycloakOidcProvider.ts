/**
 * File: /example/keycloakOidcProvider.ts
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 03-08-2022 10:50:56
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:56
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

export default class OIDCProvider {
  public authorization_endpoint: string;
  public check_session_iframe: string;
  public end_session_endpoint: string;
  public registrations_endpoint: string;
  public third_party_cookies_iframe: string;
  public token_endpoint: string;
  public userinfo_endpoint: string;
  constructor(
    keycloakBaseUrl = "http://localhost:8080",
    realm = "master",
    {
      authorizationEndpoint,
      checkSessionIframe,
      endSessionEndpoint,
      registrationsEndpoint,
      thirdPartyCookiesIframe,
      tokenEndpoint,
      userinfoEndpoint,
    }: OIDCConfig = {}
  ) {
    const realmUrl = `${keycloakBaseUrl}/realms/${realm}`;
    this.authorization_endpoint = authorizationEndpoint
      ? authorizationEndpoint
      : `${realmUrl}/protocol/openid-connect/auth`;
    this.token_endpoint = tokenEndpoint
      ? tokenEndpoint
      : `${realmUrl}/protocol/openid-connect/token`;
    this.end_session_endpoint = endSessionEndpoint
      ? endSessionEndpoint
      : `${realmUrl}/protocol/openid-connect/logout`;
    this.check_session_iframe = checkSessionIframe
      ? checkSessionIframe
      : `${realmUrl}/protocol/openid-connect/login-status-iframe.html`;
    this.third_party_cookies_iframe = thirdPartyCookiesIframe
      ? thirdPartyCookiesIframe
      : `${realmUrl}/protocol/openid-connect/3p-cookies/step1.html`;
    this.registrations_endpoint = registrationsEndpoint
      ? registrationsEndpoint
      : `${realmUrl}/protocol/openid-connect/registrations`;
    this.userinfo_endpoint = userinfoEndpoint
      ? userinfoEndpoint
      : `${realmUrl}/protocol/openid-connect/userinfo`;
  }
}

export interface OIDCConfig {
  authorizationEndpoint?: string;
  checkSessionIframe?: string;
  endSessionEndpoint?: string;
  registrationsEndpoint?: string;
  thirdPartyCookiesIframe?: string;
  tokenEndpoint?: string;
  userinfoEndpoint?: string;
}
