package com.paulgallegos.account.exception;

import java.util.UUID;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(UUID userId) {
        super("Account already exists for user id: " + userId);
    }
}
