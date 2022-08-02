/**
 * File: /example/index.tsx
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 01-08-2022 12:40:10
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import Keycloak from "keycloak-js";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import { ThemeProvider } from "theme-ui";
import { dark } from "@theme-ui/presets";
import { render } from "react-dom";
import App from "./App";

const keycloak = new Keycloak({
  url: "http://localhost:8080/auth",
  realm: "main",
  clientId: "example",
});

function handleKeycloakEvent(e: unknown, err: unknown) {
  console.log("onKeycloakEvent", e, err);
}

function handleKeycloakTokens(tokens: unknown) {
  console.log("onKeycloakTokens", tokens);
}

render(
  <ReactKeycloakProvider
    authClient={keycloak}
    onEvent={handleKeycloakEvent}
    onTokens={handleKeycloakTokens}
  >
    <ThemeProvider theme={dark}>
      <App />
    </ThemeProvider>
  </ReactKeycloakProvider>,
  document.getElementById("app")
);
