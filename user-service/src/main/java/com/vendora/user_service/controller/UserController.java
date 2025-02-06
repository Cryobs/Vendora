package com.vendora.user_service.controller;


import com.vendora.user_service.DTO.LoginDTO;
import com.vendora.user_service.DTO.RegisterDTO;
import com.vendora.user_service.service.UserService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginDTO user){
        return ResponseEntity.ok(userService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<AccessTokenResponse> register(@RequestBody RegisterDTO user){
        return ResponseEntity.ok(userService.register(user));
    }
}
