/**
 * File: /client/hooks/useKeycloakAccountAvatarClient.ts
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 03-08-2022 12:45:02
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:56
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import { useKeycloak } from "@react-keycloak/web";
import { useState, useEffect } from "react";
import KeycloakAccountAvatarClient from "../index";

export default function useKeycloakAccountAvatarClient() {
  const [keycloakAccountAvatarClient, setKeycloakAccountAvatarClient] =
    useState<KeycloakAccountAvatarClient | null>(null);
  const { keycloak } = useKeycloak();

  useEffect(() => {
    if (!keycloak?.authenticated) return;
    setKeycloakAccountAvatarClient(new KeycloakAccountAvatarClient(keycloak));
  }, [keycloak.authenticated]);

  return keycloakAccountAvatarClient;
}
