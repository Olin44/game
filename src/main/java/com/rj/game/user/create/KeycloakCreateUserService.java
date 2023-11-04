package com.rj.game.user.create;

import com.rj.game.user.domain.UserId;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@Slf4j
class KeycloakCreateUserService implements CreateUserService {
    private final KeycloakFacade keycloakFacade;

    private final UserIdExtractor userIdExtractor;

    public KeycloakCreateUserService(KeycloakFacade keycloakFacade,
                                     UserIdExtractor userIdExtractor) {
        this.keycloakFacade = keycloakFacade;
        this.userIdExtractor = userIdExtractor;
    }


    public UserId createUser(CreateUserRequest createUserRequest) {
        CredentialRepresentation credentialRepresentation = aCredentialRepresentation(createUserRequest.password());
        UserRepresentation userRepresentation = aUserRepresentation(createUserRequest, credentialRepresentation);
        UsersResource usersResource = keycloakFacade.usersResource();
        return createUserInKeycloak(userRepresentation, usersResource);
    }

    private UserId createUserInKeycloak(UserRepresentation createUserRepresentation, UsersResource usersResource) {
        try (Response response = usersResource.create(createUserRepresentation)) {
            if (response.getStatus() == 201) {
                return userIdExtractor.extract(response.getLocation());
            }
            if (response.getStatus() == 409) {
                throw new EntityExistException("User with given email already exist");
            }
            if (response.getStatus() == 503) {
                throw new ExternalServiceNotAvailableException("User service not available");
            }
            if (response.getStatus() == 500) {
                throw new ExternalServiceException("User service unexpected error");
            }
        }
        throw new RuntimeException("Error while creating user");
    }

    private UserRepresentation aUserRepresentation(CreateUserRequest createUserRequest,
                                                   CredentialRepresentation credentialRepresentation) {
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(createUserRequest.email());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(createUserRequest.firstname());
        kcUser.setLastName(createUserRequest.lastname());
        kcUser.setEmail(createUserRequest.email());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        return kcUser;
    }

    private CredentialRepresentation aCredentialRepresentation(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
