package com.rj.game.user.create;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class KeycloakFacade {

    private final Keycloak keycloak;

    private final String realm;

    public KeycloakFacade(KeycloakProperties keycloakProperties) {
        this.realm = keycloakProperties.realm();
        this.keycloak = KeycloakBuilder.builder()
                .realm(keycloakProperties.realm())
                .serverUrl(keycloakProperties.authServerUrl())
                .clientId(keycloakProperties.resource())
                .clientSecret(keycloakProperties.secret())
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
