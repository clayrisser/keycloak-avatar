/**
 * File: /example/index.tsx
 * Project: @promanager/keycloak-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 10:57:18
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

import Keycloak from "keycloak-js";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import { ThemeProvider } from "theme-ui";
import { createRoot } from "react-dom/client";
import { dark } from "@theme-ui/presets";
import App from "./App";
import OIDCProvider from "./keycloakOidcProvider";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "main",
  clientId: "example",
  oidcProvider: new OIDCProvider("http://localhost:8080", "main", {
    // tokenEndpoint: "http://localhost:3000",
  }),
} as any);

const appElement = document.getElementById("app");
if (appElement) {
  const root = createRoot(appElement);
  root.render(
    <ReactKeycloakProvider
      initOptions={{
        checkLoginIframe: true,
        checkLoginIframeInterval: 5,
        pkceMethod: "S256",
      }}
      authClient={keycloak}
    >
      <ThemeProvider theme={dark}>
        <App />
      </ThemeProvider>
    </ReactKeycloakProvider>
  );
}
