package com.vendora.user_service.controller;


import com.vendora.user_service.DTO.ChangePasswordDTO;
import com.vendora.user_service.DTO.ChangeUserDataDTO;
import com.vendora.user_service.DTO.LoginDTO;
import com.vendora.user_service.DTO.RegisterDTO;
import com.vendora.user_service.service.UserService;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{username}")
    public ResponseEntity<UserRepresentation> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserRepresentation> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/password")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Map<String, Object>> changePassword(@AuthenticationPrincipal Jwt jwt, @RequestBody ChangePasswordDTO changePasswordDTO){
        return ResponseEntity.ok(userService.changePassword(jwt, changePasswordDTO));
    }

    @PutMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Map<String, Object>> changeUserData(@AuthenticationPrincipal Jwt jwt, @RequestBody ChangeUserDataDTO changeUserDataDTO){
        return ResponseEntity.ok(userService.changeUserData(jwt, changeUserDataDTO));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Map<String, Object>> deleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.deleteUser(jwt, username));
    }

    @PutMapping("/admin/{username}/roles")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, Object>> assignRoles(@PathVariable String username, @RequestBody List<String> roles) {
        return ResponseEntity.ok(userService.assignRoles(username, roles));
    }

    @DeleteMapping("/admin/{username}/roles")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, Object>> deleteRoles(@PathVariable String username, @RequestBody List<String> roles) {
        return ResponseEntity.ok(userService.deleteRoles(username, roles));
    }
}
