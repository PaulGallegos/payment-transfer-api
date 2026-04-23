package com.paulgallegos.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
