package com.paulgallegos.auth.service;

import com.paulgallegos.auth.dto.LoginRequest;
import com.paulgallegos.auth.dto.RefreshRequest;
import com.paulgallegos.auth.dto.RegisterRequest;
import com.paulgallegos.auth.dto.AuthResponse;
import com.paulgallegos.auth.entity.User;
import com.paulgallegos.auth.exception.InvalidCredentialsException;
import com.paulgallegos.auth.exception.UserEmailDuplicateException;
import com.paulgallegos.auth.repository.UserRepository;
import com.paulgallegos.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest registerRequest){

        userRepository.findByEmail(registerRequest.getEmail()).ifPresent(user -> {throw new UserEmailDuplicateException(user.getEmail());});

        User user = User.builder()
                .role(registerRequest.getRole())
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .enabled(true)
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        var savedUser = userRepository.save(user);

        TokenPair tokens = generateTokens(user);

        return  AuthResponse.builder()
                .role(savedUser.getRole())
                .token(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .userId(savedUser.getId())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));



        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPasswordHash()))
            throw new InvalidCredentialsException("Invalid credentials");

        TokenPair tokens = generateTokens(user);

        return AuthResponse.builder()
                .role(user.getRole())
                .token(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .userId(user.getId())
                .build();
    }

    public AuthResponse refresh(RefreshRequest refreshRequest) {
        String userId = refreshRequest.getUserId();
        String refreshToken = refreshRequest.getRefreshToken();

        if (!refreshTokenService.validate(userId, refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .userId(user.getId())
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    public void logout(RefreshRequest refreshRequest) {
        refreshTokenService.delete(refreshRequest.getUserId());
    }

    private TokenPair generateTokens(User user) {
        String userId = user.getId().toString();
        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(userId);
        refreshTokenService.save(userId, refreshToken);
        return new TokenPair(accessToken, refreshToken);
    }

    private record TokenPair(String accessToken, String refreshToken) {}

}
