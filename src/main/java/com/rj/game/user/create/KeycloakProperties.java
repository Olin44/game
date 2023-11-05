package com.rj.game.user.create;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak")
public record KeycloakProperties(String authServerUrl,
                                 String realm,
                                 String resource,
                                 String secret) {
}
