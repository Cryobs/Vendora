package com.vendora.notification_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.jwt.Jwt;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;

    public OAuth2FeignRequestInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void apply(RequestTemplate template) {

        if(template.url().contains("/cart")){
            String token = getUserJwt();
            if (token != null){
                template.header("Authorization", "Bearer " + token);
            }
        } else {
            String token = getClientJwt();
            if (token != null){
                template.header("Authorization", "Bearer " + token);
            }
        }
    }

    private String getClientJwt(){
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal("notification-service")
                .build();

        return manager.authorize(request)
                .getAccessToken()
                .getTokenValue();
    }

    private String getUserJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        return null;
    }

}

