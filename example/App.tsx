/**
 * File: /example/App.tsx
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 01-08-2022 12:26:57
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import React, { FC, useCallback, useEffect } from "react";
import { Flex, Heading, Button } from "theme-ui";
import KeycloakAccountAvatarClient from "../client";
import useKeycloak from "./hooks/useKeycloak";

export interface AppProps {}

const App: FC<AppProps> = () => {
  const keycloak = useKeycloak();

  useEffect(() => {
    keycloak.init({
      onLoad: "login-required",
      redirectUri: "https://example.com",
    });
  }, []);

  return (
    <Flex>
      <Heading>Keycloak Account Avatar</Heading>
    </Flex>
  );
};

export default App;
