package com.paulgallegos.auth.dto;

import com.paulgallegos.auth.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private Long userId;
    private String token;
    private Role role;

}
