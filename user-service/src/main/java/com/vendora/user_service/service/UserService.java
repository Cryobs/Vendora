package com.vendora.user_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendora.user_service.DTO.LoginDTO;
import com.vendora.user_service.DTO.RegisterDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.OAuth2Constants;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.keycloak.OAuth2Constants;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final WebClient webClient;
    private final String keycloakUrl;
    private final String clientId;
    private final String clientSecret;
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;



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

    public AccessTokenResponse login(LoginDTO user){
        System.out.println("test");
        Keycloak loginKeycloak = KeycloakBuilder.builder()
                .grantType("password")
                .password(user.getPassword())
                .username(user.getUsername())
                .realm(realm)
                .serverUrl(keycloakUrl)
                .clientId("vendora-rest-api")
                .build();
        System.out.println("test2");
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


    public String getAccessToken() {
        String response = webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            // Преобразуем строку в JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);

            // Извлекаем access_token
            return jsonResponse.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing access token from response", e);
        }
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



}
