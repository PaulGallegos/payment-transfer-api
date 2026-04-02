package com.paulgallegos.account.controller;


import com.paulgallegos.account.dto.AccountResponse;
import com.paulgallegos.account.dto.CreateAccountRequest;
import com.paulgallegos.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody CreateAccountRequest request){
        return accountService.createAccount(userId, request);
    }

    @GetMapping("/me")
    public AccountResponse getAccount(@RequestHeader("X-User-Id") UUID userId){
        return accountService.getAccount(userId);
    }

}
