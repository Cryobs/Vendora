package com.vendora.user_service.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.host-uri}")
    private String keycloakUrl;

    @Value("${spring.security.oauth2.client.registration.user-service-client.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.user-service-client.client-secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
