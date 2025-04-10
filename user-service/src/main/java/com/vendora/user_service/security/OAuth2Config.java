package com.vendora.user_service.security;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class OAuth2Config {

//    @Value("${spring.security.oauth2.client.registration.user-service-client.client-secret}")
//    private String clientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.user-service-client.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
//    private String tokenUri;
//
//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("user-service-client")
//                .tokenUri("http://localhost:8090/realms/myrealm/protocol/openid-connect/token")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .build();
//
//        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
//    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager manager) {
        return new OAuth2FeignRequestInterceptor(manager);
    }
}
