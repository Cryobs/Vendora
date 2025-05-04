package com.vendora.user_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeUserDataDTO {
    private String username = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
}
