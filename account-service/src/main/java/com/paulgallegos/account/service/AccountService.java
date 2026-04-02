package com.paulgallegos.account.service;

import com.paulgallegos.account.domain.Account;
import com.paulgallegos.account.domain.AccountStatus;
import com.paulgallegos.account.dto.AccountResponse;
import com.paulgallegos.account.dto.CreateAccountRequest;
import com.paulgallegos.account.exception.AccountAlreadyExistsException;
import com.paulgallegos.account.exception.AccountNotFoundException;
import com.paulgallegos.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(UUID userId, CreateAccountRequest request){

        accountRepository.findByUserId(userId).ifPresent(account -> {throw new AccountAlreadyExistsException(userId);});

        var account = accountRepository.saveAndFlush(Account.builder().userId(userId)
                        .accountNumber("ACC-"+UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .balance(request.getInitialBalance())
                        .currency(request.getCurrency())
                        .status(AccountStatus.ACTIVE)
                        .build());

        log.info("Account created: {} for user: {}", account.getAccountNumber(), userId);

        return toResponse(account);

    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID userId){
        return accountRepository.findByUserId(userId).map(this::toResponse).orElseThrow(() -> new AccountNotFoundException(userId));
    }



    private AccountResponse toResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }
}
