package com.paulgallegos.payment.controller;

import com.paulgallegos.payment.dto.CreatePaymentRequest;
import com.paulgallegos.payment.dto.PaymentResponse;
import com.paulgallegos.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody CreatePaymentRequest paymentRequest){
        return paymentService.createPayment(userId, paymentRequest);
    }



}
