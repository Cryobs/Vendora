package com.vendora.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
