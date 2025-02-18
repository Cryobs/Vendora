package com.vendora.user_service.service;

import com.vendora.user_service.DTO.ChangePasswordDTO;
import com.vendora.user_service.DTO.ChangeUserDataDTO;
import com.vendora.user_service.DTO.LoginDTO;
import com.vendora.user_service.DTO.RegisterDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Service
public class UserService {

    private final WebClient webClient;
    private final String keycloakUrl;
    private final String clientId;
    private final String clientSecret;
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${jwt.auth.converter.principalAttribute}")
    private String principalAttribute;



    @Autowired
    public UserService(WebClient.Builder webClientBuilder,
                       Keycloak keycloak,  // Теперь просто внедряем Bean
                       @Value("${keycloak.host-uri}") String keycloakUrl,
                       @Value("${spring.security.oauth2.client.registration.user-service-client.client-id}") String clientId,
                       @Value("${spring.security.oauth2.client.registration.user-service-client.client-secret}") String clientSecret) {
        this.keycloak = keycloak;
        this.webClient = webClientBuilder.baseUrl(keycloakUrl).build();
        this.keycloakUrl = keycloakUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public UserRepresentation getUserByUsername(String username){
        return keycloak.realm(realm).users().searchByUsername(username, true).getFirst();
    }

    public AccessTokenResponse login(LoginDTO user){
        Keycloak loginKeycloak = KeycloakBuilder.builder()
                .grantType("password")
                .password(user.getPassword())
                .username(user.getUsername())
                .realm(realm)
                .serverUrl(keycloakUrl)
                .clientId("vendora-rest-api")
                .build();
        return loginKeycloak.tokenManager().getAccessToken();
    }

    public AccessTokenResponse register(RegisterDTO userReg) {
        //define user
        UserRepresentation user = new UserRepresentation();
        //set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userReg.getPassword());

        //set all user data
        user.setUsername(userReg.getUsername());
        user.setCredentials(List.of(credential));
        user.setEnabled(true);
        user.setEmail(userReg.getEmail());
        user.setFirstName(userReg.getFirstName());
        user.setLastName(userReg.getLastName());
        user.setRealmRoles(List.of("user"));


        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        //create user
        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        //set roles
        UserResource userResource = usersResource.get(userId);
        RoleRepresentation testerRealmRole = realmResource.roles()
                .get("user").toRepresentation();
        userResource.roles().realmLevel() //
                .add(List.of(testerRealmRole));

        //get token
        LoginDTO loginDTO = new LoginDTO(
                userReg.getUsername(),
                userReg.getPassword());

        return login(loginDTO);
    }

    private Map<String, Object> createUserPayload(RegisterDTO user) {
        return Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", user.getPassword(),
                        "temporary", false
                )),
                "realmRoles", List.of(Map.of("name","user"))
        );
    }


    public Map<String, Object> changePassword(Jwt jwt, ChangePasswordDTO changePasswordDTO){
        if (!Objects.equals(changePasswordDTO.getNewPassword(), changePasswordDTO.getRepeatNewPassword())){
            throw new IllegalArgumentException("The new passwords are not the same");
        } else if (Objects.equals(changePasswordDTO.getNewPassword(), changePasswordDTO.getCurrentPassword())){
            throw new IllegalArgumentException("The new password cannot be the same as current");
        }
        else {
            try {
                //test current password
                LoginDTO testPass = new LoginDTO(jwt.getClaimAsString(principalAttribute), changePasswordDTO.getCurrentPassword());
                login(testPass);

                //get user
                RealmResource realmResource = keycloak.realm(realm);
                UserResource userResource = realmResource.users().get(jwt.getSubject());

                //define password credentials
                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(false);
                credentialRepresentation.setType("password");
                credentialRepresentation.setValue(changePasswordDTO.getNewPassword());

                //change password
                userResource.resetPassword(credentialRepresentation);
                return Map.of("message", "Password changed");
            } catch (Exception e){
                throw e;
            }
        }
    }


    public Map<String, Object> changeUserData(Jwt jwt, ChangeUserDataDTO newUserData){
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(jwt.getSubject());
        UserRepresentation newUser = userResource.toRepresentation();
        if (newUserData.getEmail() != null){
            newUser.setEmail(newUserData.getEmail());
        }
        if (newUserData.getUsername() != null){
            newUser.setUsername(newUserData.getUsername());
        }
        if (newUserData.getFirstName() != null){
            newUser.setFirstName(newUserData.getFirstName());
        }
        if (newUserData.getLastName() != null){
            newUser.setLastName(newUserData.getLastName());
        }
        userResource.update(newUser);
        return Map.of("message", "User data changed");
    }

    public Map<String, Object> deleteUser(Jwt jwt, String username) throws Exception {
        //user role
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> userRoles = (Collection<String>) realmAccess.get("roles");

        if (Objects.equals(jwt.getClaimAsString(principalAttribute), username) || userRoles.contains("admin")){
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation userRepresentation = realmResource.users().searchByUsername(username, true).getFirst();
            UserResource userResource = realmResource.users().get(userRepresentation.getId());
            userResource.remove();
            return Map.of("message", "User deleted");
        }

        throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Unauthorized");
    }
    public Map<String, Object> assignRoles(String username, List<String> roles){
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRepresentation = realmResource.users().searchByUsername(username, true).getFirst();
        UserResource userResource = realmResource.users().get(userRepresentation.getId());

        List<RoleRepresentation> roleList = new ArrayList<>();
        for (String el : roles){
            RoleRepresentation roleRepresentation = realmResource.roles().get(el).toRepresentation();
            roleList.add(roleRepresentation);
        }

        userResource.roles().realmLevel().add(roleList);

        return Map.of("message", "Roles assigned");
    }

    public Map<String, Object> deleteRoles(String username, List<String> roles){
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRepresentation = realmResource.users().searchByUsername(username, true).getFirst();
        UserResource userResource = realmResource.users().get(userRepresentation.getId());

        List<RoleRepresentation> roleList = new ArrayList<>();
        for (String el : roles){
            RoleRepresentation roleRepresentation = realmResource.roles().get(el).toRepresentation();
            roleList.add(roleRepresentation);
        }

        userResource.roles().realmLevel().remove(roleList);

        return Map.of("message", "Roles removed");
    }

}
