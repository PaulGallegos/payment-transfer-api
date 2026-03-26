package com.paulgallegos.payment.exception;

public class DuplicatePaymentException extends RuntimeException{


    public DuplicatePaymentException(String idempotencyKey) {
        super("Payment already exists with idempotency key: " + idempotencyKey);
    }
}
