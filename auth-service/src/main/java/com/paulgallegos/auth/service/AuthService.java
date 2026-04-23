package com.paulgallegos.auth.service;

import com.paulgallegos.auth.dto.LoginRequest;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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


        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole());

        return  AuthResponse.builder()
                .role(savedUser.getRole())
                .token(token)
                .userId(savedUser.getId())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isPresent()){

            if(!passwordEncoder.matches(loginRequest.getPassword(),user.get().getPasswordHash()))
                throw new InvalidCredentialsException("Invalid credentials");

            return AuthResponse.builder()
                    .role(user.get().getRole())
                    .token(jwtService.generateToken(user.get().getEmail(), user.get().getRole()))
                    .userId(user.get().getId())
                    .build();
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }

}
