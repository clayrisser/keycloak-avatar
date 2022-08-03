/**
 * File: /example/App.tsx
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 03-08-2022 13:17:04
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import React, { FC, useEffect, useCallback, useState } from "react";
import { Flex, Heading, Text, Button, Input, Label } from "theme-ui";
import { useKeycloak } from "@react-keycloak/web";
import useKeycloakAccountAvatarClient from "../client/hooks/useKeycloakAccountAvatarClient";

export interface AppProps {}

const App: FC<AppProps> = () => {
  const { keycloak } = useKeycloak();
  const [avatarFile, setAvatarFile] = useState<Blob | null>(null);
  const keycloakAccountAvatarClient = useKeycloakAccountAvatarClient();

  useEffect(() => {
    if (!keycloak.authenticated) return;
    console.log("YAY logged in");
  }, [keycloak.authenticated]);

  useEffect(() => {
    if (!keycloakAccountAvatarClient) return;
    console.log("keycloakAccountAvatarClient", keycloakAccountAvatarClient);
    console.log("my avatar", keycloakAccountAvatarClient.accountAvatarUrl);
  }, [keycloakAccountAvatarClient]);

  const handleUploadAvatar = useCallback(() => {
    if (!keycloakAccountAvatarClient || !avatarFile) return;
    (async () => {
      const result = await keycloakAccountAvatarClient.uploadAccountAvatar(
        avatarFile
      );
      console.log("result", result);
    })();
  }, [keycloakAccountAvatarClient, avatarFile]);

  return (
    <Flex sx={{ flexDirection: "column" }}>
      <Heading>Keycloak Account Avatar</Heading>
      <Text>User is {!keycloak.authenticated ? "NOT " : ""} authenticated</Text>
      <Button
        onClick={() => keycloak.login({ scope: "openid" })}
        sx={{ cursor: "pointer" }}
      >
        Login
      </Button>
      <Label>
        <Input
          onChange={(e) =>
            setAvatarFile(e.target.files?.length ? e.target.files[0] : null)
          }
          type="file"
        />
        <Button onClick={handleUploadAvatar}>Upload Avatar</Button>
      </Label>
    </Flex>
  );
};

export function createSecureOidcProvider() {}

export default App;
