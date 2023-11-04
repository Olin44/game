package com.rj.game.user;

import com.rj.game.auth.config.KeycloakProvider;
import com.rj.game.user.create.CreateUserRequest;
import com.rj.game.user.create.CreateUserService;
import com.rj.game.user.domain.UserId;
import com.rj.game.user.login.LoginRequest;
import jakarta.validation.constraints.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final CreateUserService createUserService;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public UserController(CreateUserService createUserService, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.createUserService = createUserService;
    }


    @PostMapping(value = "/create")
    public UserId createUser(@RequestBody CreateUserRequest user) {
        return createUserService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.username(), loginRequest.password()).build();
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
    }
}
