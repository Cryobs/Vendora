package com.vendora.notification_service.feign;

import com.vendora.notification_service.security.OAuth2FeignConfig;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", configuration = OAuth2FeignConfig.class)
public interface UserClient {
    @GetMapping("/id/{userId}")
    ResponseEntity<UserRepresentation> getUserById(@PathVariable String userId);
}

