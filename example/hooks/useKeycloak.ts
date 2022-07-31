/**
 * File: /example/hooks/useKeycloak.ts
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:19:34
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 15:38:35
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import { useMemo } from "react";
import Keycloak from "keycloak-js";

export default function useKeycloak(): Keycloak {
  return useMemo(
    () =>
      new Keycloak({
        url: "http://localhost:8080",
        realm: "main",
        clientId: "example",
      }),
    []
  );
}
