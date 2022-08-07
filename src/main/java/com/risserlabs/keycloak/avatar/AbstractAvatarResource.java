/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * /src/main/java/com/risserlabs/keycloak/avatar/AbstractAvatarResource.java
 * Project: @risserlabs/keycloak-account-avatar-client
 * File Created: 30-07-2022 12:03:15
 * Author: Clay Risser
 * -----
 * Last Modified: 07-08-2022 13:09:27
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar;

import com.risserlabs.keycloak.avatar.idpServices.IdpService;
import com.risserlabs.keycloak.avatar.storage.AvatarStorageProvider;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.resources.Cors;

public abstract class AbstractAvatarResource {
  private final Logger logger = Logger.getLogger(AbstractAvatarResource.class);
  protected static final String AVATAR_IMAGE_PARAMETER = "image";
  protected KeycloakSession session;

  public AbstractAvatarResource(KeycloakSession session) {
    this.session = session;
  }

  protected AvatarStorageProvider lookupAvatarStorageProvider(KeycloakSession keycloakSession) {
    return keycloakSession.getProvider(AvatarStorageProvider.class);
  }

  public AvatarStorageProvider getAvatarStorageProvider() {
    return lookupAvatarStorageProvider(session);
  }

  protected Response badRequest() {
    return badRequest("", null);
  }

  protected Response badRequest(String errorMessage) {
    return badRequest(errorMessage, null);
  }

  protected Response badRequest(Cors cors) {
    return badRequest("", cors);
  }

  protected Response badRequest(String errorMessage, Cors cors) {
    String body = "";
    String mediaType = MediaType.TEXT_PLAIN;
    if (errorMessage != null && errorMessage.length() > 0) {
      body = Json.createObjectBuilder()
          .add("error", errorMessage).build().toString();
      mediaType = MediaType.APPLICATION_JSON;
    }
    ResponseBuilder responseBuilder = Response
        .ok(body, mediaType)
        .status(Response.Status.BAD_REQUEST);
    if (cors != null) {
      return cors.builder(responseBuilder).build();
    }
    return responseBuilder.build();
  }

  protected Response unauthorized() {
    return unauthorized(null);
  }

  protected Response unauthorized(Cors cors) {
    ResponseBuilder responseBuilder = Response
        .ok("", MediaType.TEXT_PLAIN)
        .status(Response.Status.UNAUTHORIZED);
    if (cors != null) {
      return cors.builder(responseBuilder).build();
    }
    return responseBuilder.build();
  }

  protected void uploadAvatarImage(String realmName, String userId, InputStream imageInputStream) {
    getAvatarStorageProvider().uploadAvatarImage(realmName, userId, imageInputStream);
  }

  protected StreamingOutput downloadAvatarImage(String realmId, String userId) {
    InputStream stream = getAvatarStorageProvider().downloadAvatarImage(realmId, userId);
    if (stream == null) {
      return null;
    }
    return (output) -> AvatarUtil.copyStream(stream, output);
  }

  protected StreamingOutput getSinglePixelImage() {
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Color black = new Color(0, 0, 0);
    image.setRGB(0, 0, black.getRGB());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", outputStream);
    } catch (IOException ex) {
      logger.error(ex);
    }
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    return (output) -> AvatarUtil.copyStream(inputStream, output);
  }

  protected InputStream downloadFederatedIdentityAvatarImage(FederatedIdentityModel federatedIdentity) {
    IdpService idpService = IdpService.getIdpService(federatedIdentity.getIdentityProvider());
    if (idpService == null) {
      return null;
    }
    return idpService.downloadAvatarImage(federatedIdentity.getToken());
  }

  protected StreamingOutput downloadAndSaveFederatedIdentityAvatarImage(
      FederatedIdentityModel federatedIdentity,
      String realmName,
      String userId) {
    InputStream stream = downloadFederatedIdentityAvatarImage(federatedIdentity);
    if (stream == null) {
      return null;
    }
    byte[] imageBytes = streamToByteArray(stream);
    uploadAvatarImage(realmName, userId, new ByteArrayInputStream(imageBytes));
    return (output) -> AvatarUtil.copyStream(new ByteArrayInputStream(imageBytes), output);
  }

  protected FederatedIdentityModel getFirstFederatedIdentity(AuthResult authResult) {
    UserModel user = authResult.getUser();
    if (user == null) {
      return null;
    }
    ArrayList<FederatedIdentityModel> federatedIdentities = getFederatedIdentities(user);
    if (federatedIdentities.size() > 0) {
      return federatedIdentities.get(0);
    }
    return null;
  }

  protected String getRealmName(UriInfo uriInfo) {
    String realmName = session.getContext().getRealm().getName();
    if (realmName != null) {
      return realmName;
    }
    realmName = "master";
    Pattern pattern = Pattern.compile("/realms/[^/]+", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(uriInfo.getPath());
    if (matcher.find()) {
      realmName = matcher.group(0).substring(8);
    }
    return realmName;
  }

  protected byte[] streamToByteArray(InputStream stream) {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int nRead;
      byte[] data = new byte[4];
      while ((nRead = stream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      logger.error(e);
      return null;
    }
  }

  private ArrayList<FederatedIdentityModel> getFederatedIdentities(UserModel user) {
    RealmModel realm = session.getContext().getRealm();
    Set<String> idps = realm.getIdentityProvidersStream().map(IdentityProviderModel::getAlias)
        .collect(Collectors.toSet());
    Stream<FederatedIdentityModel> federatedIdentitiesStream = session.users()
        .getFederatedIdentitiesStream(realm, user)
        .filter(identity -> idps.contains(identity.getIdentityProvider()));
    return new ArrayList<FederatedIdentityModel>(federatedIdentitiesStream.collect(Collectors.toList()));
  }
}
