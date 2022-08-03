/**
 * File: /example/App.tsx
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 03-08-2022 10:51:05
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import React, { FC, useEffect } from "react";
import { Flex, Heading, Text, Button } from "theme-ui";
import { useKeycloak } from "@react-keycloak/web";

export interface AppProps {}

const App: FC<AppProps> = () => {
  const { keycloak } = useKeycloak();
  console.log("keycloak", keycloak);

  // useEffect(() => {
  //   keycloak.parseCallback

  // }, []);

  return (
    <Flex sx={{ flexDirection: "column" }}>
      <Heading>Keycloak Account Avatar</Heading>
      <Text>User is {!keycloak.authenticated ? "NOT " : ""} authenticated</Text>
      <Button onClick={() => keycloak.login({ scope: "openid" })}>
        Login{" "}
      </Button>
    </Flex>
  );
};

export function createSecureOidcProvider() {}

export default App;
