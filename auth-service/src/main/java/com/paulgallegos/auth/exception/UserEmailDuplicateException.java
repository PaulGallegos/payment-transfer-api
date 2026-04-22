package com.paulgallegos.auth.exception;

public class UserEmailDuplicateException extends RuntimeException {
    public UserEmailDuplicateException(String email) {
        super("User already exists with email: " + email);
    }
}
