package com.vendora.order_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;

    public OAuth2FeignRequestInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void apply(RequestTemplate template) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal("order-service")
                .build();

        String token = manager.authorize(request)
                .getAccessToken()
                .getTokenValue();

        template.header("Authorization", "Bearer " + token);
    }
}

