package com.paulgallegos.auth.controller;

import com.paulgallegos.auth.dto.LoginRequest;
import com.paulgallegos.auth.dto.RefreshRequest;
import com.paulgallegos.auth.dto.RegisterRequest;
import com.paulgallegos.auth.dto.AuthResponse;
import com.paulgallegos.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody @Valid RegisterRequest registerRequest){

        return authService.register(registerRequest);

    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        return authService.refresh(refreshRequest);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid RefreshRequest refreshRequest) {
        authService.logout(refreshRequest);
    }

}
