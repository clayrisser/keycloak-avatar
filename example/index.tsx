/**
 * File: /example/index.tsx
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 31-07-2022 15:08:23
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import { dark } from "@theme-ui/presets";
import { render } from "react-dom";
import { ThemeProvider } from "theme-ui";
import App from "./App";

render(
  <ThemeProvider theme={dark}>
    <App />
  </ThemeProvider>,
  document.getElementById("app")
);
