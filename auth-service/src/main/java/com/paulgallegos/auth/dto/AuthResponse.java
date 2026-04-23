package com.paulgallegos.auth.dto;

import com.paulgallegos.auth.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {

    private UUID userId;
    private String token;
    private Role role;

}
