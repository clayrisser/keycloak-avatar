/**
 * File: /example/App.tsx
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 31-07-2022 15:02:39
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:56
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

import React, { FC, useCallback, useState, useEffect } from "react";
import { Flex, Heading, Image, Button, Input, Box } from "theme-ui";
import { useKeycloak } from "@react-keycloak/web";
import useKeycloakAccountAvatarClient from "../client/hooks/useKeycloakAccountAvatarClient";

export interface AppProps {}

const App: FC<AppProps> = () => {
  const { keycloak } = useKeycloak();
  const [avatarFile, setAvatarFile] = useState<Blob | null>(null);
  const [avatarUrl, setAvatarUrl] = useState<string | null>(null);
  const [avatarNonce, setAvatarNonce] = useState(0);
  const [uploading, setUploading] = useState(false);
  const keycloakAccountAvatarClient = useKeycloakAccountAvatarClient();

  useEffect(() => {
    if (!keycloakAccountAvatarClient) return;
    const avatarUrl = keycloakAccountAvatarClient.getAccountAvatarUrl(
      undefined,
      avatarNonce
    );
    if (avatarUrl) setAvatarUrl(avatarUrl);
  }, [keycloakAccountAvatarClient, avatarNonce]);

  const handleUploadAvatar = useCallback(() => {
    if (!keycloakAccountAvatarClient || !avatarFile) return;
    setUploading(true);
    (async () => {
      await keycloakAccountAvatarClient.uploadAccountAvatar(avatarFile);
      await new Promise((r) => setTimeout(r, 1000));
      setAvatarFile(null);
      setUploading(false);
      setAvatarNonce((avatarNonce: number) => avatarNonce + 1);
    })();
  }, [keycloakAccountAvatarClient, avatarFile]);

  return (
    <Flex sx={{ flexDirection: "column" }}>
      <Flex sx={{ flexDirection: "column", padding: 20, alignItems: "center" }}>
        <Box sx={{ margin: 10 }}>
          <Heading>Keycloak Account Avatar</Heading>
        </Box>
        <Button
          onClick={() => keycloak.login({ scope: "openid" })}
          disabled={keycloak.authenticated}
          sx={{
            alignSelf: "center",
            cursor: keycloak.authenticated ? "inherit" : "pointer",
            bg: keycloak.authenticated ? "muted" : "primary",
            margin: 10,
          }}
        >
          {keycloak.authenticated ? "Logged In" : "Login"}
        </Button>
      </Flex>
      <Flex sx={{ padding: 20, justifyContent: "center" }}>
        <Input
          disabled={uploading || !keycloak.authenticated}
          onChange={(e) =>
            setAvatarFile(e.target.files?.length ? e.target.files[0] : null)
          }
          type="file"
          sx={{
            alignSelf: "flex-start",
            cursor:
              uploading || !keycloak.authenticated ? "inherit" : "pointer",
            margin: 10,
            width: 260,
          }}
        />
        <Button
          onClick={handleUploadAvatar}
          disabled={uploading || !keycloak.authenticated || !avatarFile}
          sx={{
            alignSelf: "flex-start",
            cursor:
              uploading || !keycloak.authenticated || !avatarFile
                ? "inherit"
                : "pointer",
            margin: 10,
            width: 180,
            bg:
              uploading || !keycloak.authenticated || !avatarFile
                ? "muted"
                : "secondary",
          }}
        >
          {uploading ? "Uploading" : "Upload"} Avatar
        </Button>
      </Flex>
      <Flex sx={{ flexDirection: "column", padding: 20, alignItems: "center" }}>
        {avatarUrl ? (
          <Image
            sx={{
              bg: "white",
              height: "auto",
              margin: 20,
              maxWidth: "400px",
            }}
            src={avatarUrl}
          />
        ) : (
          []
        )}
      </Flex>
    </Flex>
  );
};

export default App;
