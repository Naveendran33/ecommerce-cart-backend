package com.project.ecommerse_card_backend.dto.userdto;

import com.project.ecommerse_card_backend.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
