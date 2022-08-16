/**
 * File: /client/index.ts
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 31-07-2022 14:50:24
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:56
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import type Keycloak from "keycloak-js";
import axios from "axios";

export default class KeycloakAccountAvatarClient {
  private http = axios.create();
  private realmUrl?: string;
  public accountAvatarUrl?: string;

  constructor(private readonly keycloak: Keycloak, realmUrl?: string) {
    this.realmUrl = realmUrl || this.keycloak.tokenParsed?.iss;
    this.accountAvatarUrl = `${this.realmUrl}/avatar`;
  }

  async getUserInfo() {
    return this.http.get(`${this.realmUrl}/protocol/openid-connect/userinfo`, {
      headers: {
        Authorization: `Bearer ${this.keycloak.token}`,
      },
    });
  }

  getAccountAvatarUrl(userId?: string, nonce?: number | string): string | null {
    if ((!userId && !this.keycloak.subject) || !this.accountAvatarUrl) {
      return null;
    }
    return `${this.accountAvatarUrl}${userId ? `/${userId}` : ""}${
      typeof nonce !== "undefined" ? `?${nonce}` : ""
    }`;
  }

  async uploadAccountAvatar(image: Blob) {
    if (!this.accountAvatarUrl) {
      throw new Error("must login to upload account avatar");
    }
    const formData = new FormData();
    formData.append("image", image);
    return this.http.post(this.accountAvatarUrl, formData, {
      headers: {
        Authorization: `Bearer ${this.keycloak.token}`,
      },
    });
  }
}
