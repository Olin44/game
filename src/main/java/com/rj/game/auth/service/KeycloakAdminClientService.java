package com.rj.game.auth.service;

import com.rj.game.auth.config.KeycloakProvider;
import com.rj.game.auth.requests.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;


@Service
@Slf4j
public class KeycloakAdminClientService {
    private final String realm;

    private final KeycloakProvider kcProvider;


    public KeycloakAdminClientService(KeycloakProvider keycloakProvider,
                                      @Value("${keycloak.realm}") String realm) {
        this.kcProvider = keycloakProvider;
        this.realm = realm;
    }

    public Response createKeycloakUser(CreateUserRequest user) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        Response response = null;
        try {
            return usersResource.create(kcUser);
        } catch (Exception exception) {
            log.error("Error creating user", exception);
        }
        return response;

//        if (response.getStatus() == 201) {
//            //If you want to save the user to your other database, do it here, for example:
////            User localUser = new User();
////            localUser.setFirstName(kcUser.getFirstName());
////            localUser.setLastName(kcUser.getLastName());
////            localUser.setEmail(user.getEmail());
////            localUser.setCreatedDate(Timestamp.from(Instant.now()));
////            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
////            usersResource.get(userId).sendVerifyEmail();
////            userRepository.save(localUser);
//        }
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


}
