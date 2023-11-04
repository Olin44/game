package com.rj.game.user.create;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class KeycloakFacade {

    private final Keycloak keycloak;

    private final String realm;

    public KeycloakFacade(@Value("${keycloak.auth-server-url}") String serverURL,
                          @Value("${keycloak.realm}") String realm,
                          @Value("${keycloak.resource}") String resource,
                          @Value("${keycloak.credentials.secret}") String clientSecret) {
        this.realm = realm;
        this.keycloak = KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(resource)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public UsersResource usersResource() {
        return keycloak.realm(realm).users();
    }

    @PreDestroy
    public void destroy() {
        keycloak.close();
    }
}
