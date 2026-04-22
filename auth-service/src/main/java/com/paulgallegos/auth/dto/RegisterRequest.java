package com.paulgallegos.auth.dto;

import com.paulgallegos.auth.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;
    @NotBlank
    private String email;
    @NotNull
    private Role role;
    @NotBlank
    private String password;
}
