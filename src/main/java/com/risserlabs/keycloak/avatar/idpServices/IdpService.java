/**
 * File: /src/main/java/com/risserlabs/keycloak/avatar/idpServices/IdpService.java
 * Project: @risserlabs/keycloak-avatar-client
 * File Created: 07-08-2022 05:10:37
 * Author: Clay Risser
 * -----
 * Last Modified: 16-08-2022 11:56:54
 * Modified By: Clay Risser
 * -----
 * Risser Labs LLC (c) Copyright 2022
 */

package com.risserlabs.keycloak.avatar.idpServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jboss.logging.Logger;

public abstract class IdpService {
  public static GithubIdpService github = new GithubIdpService();

  public static Map<String, IdpService> getIdpServices() {
    Map<String, IdpService> idpServices = new HashMap<String, IdpService>();
    idpServices.put("github", IdpService.github);
    return idpServices;
  }

  public static IdpService getIdpService(String idpServiceName) {
    return IdpService.getIdpServices().get(idpServiceName);
  }

  public InputStream downloadAvatarImage(String idpToken) {
    String bearerToken = getBearerToken(idpToken);
    String avatarUrl = getAvatarUrl(idpToken);
    if (avatarUrl == null || avatarUrl == "") {
      return null;
    }
    Request request = new Request.Builder()
        .url(avatarUrl)
        .get()
        .addHeader("Authorization", "Bearer " + bearerToken)
        .build();
    try {
      return client.newCall(request).execute().body().byteStream();
    } catch (IOException e) {
      logger.error(e);
      return null;
    }
  }

  protected final Logger logger = Logger.getLogger(IdpService.class);
  protected final OkHttpClient client = new OkHttpClient();

  protected JsonObject getUser(String bearerToken) {
    Request request = new Request.Builder()
        .url(getUserEndpoint())
        .get()
        .addHeader("Authorization", "Bearer " + bearerToken)
        .build();
    try {
      JsonReader jsonReader = Json.createReader(new StringReader(client.newCall(request).execute().body().string()));
      JsonObject body = jsonReader.readObject();
      jsonReader.close();
      return body;
    } catch (IOException e) {
      logger.error(e);
      return null;
    }
  }

  protected String getAvatarUrl(String jsonPath, String idpToken) {
    JsonObject user = getUser(getBearerToken(idpToken));
    try {
      return user.getString(jsonPath);
    } catch (NullPointerException e) {
      return null;
    }
  }

  protected String getBearerToken(String idpToken) {
    Map<String, String> idpTokenMap = splitQuery(idpToken);
    return idpTokenMap.get("access_token");
  }

  protected abstract String getUserEndpoint();

  protected abstract String getAvatarUrl(String idpToken);

  protected static Map<String, String> splitQuery(String query) {
    Map<String, String> queryPairs = new LinkedHashMap<String, String>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      try {
        queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
      }
    }
    return queryPairs;
  }
}
