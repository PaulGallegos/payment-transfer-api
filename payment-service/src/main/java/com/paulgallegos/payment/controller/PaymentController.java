package com.paulgallegos.payment.controller;

import com.paulgallegos.payment.dto.CreatePaymentRequest;
import com.paulgallegos.payment.dto.PaymentResponse;
import com.paulgallegos.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@RequestHeader("X-User-Id") UUID userId, @PathVariable("paymentId") UUID paymentId){
        return paymentService.getPayment(userId, paymentId);
    }

    @GetMapping("/history")
    public Page<PaymentResponse> getPaymentHistory(@RequestHeader("X-User-Id") UUID userId, @PageableDefault(size = 20, sort = "createdAt") Pageable pageable){
        return paymentService.getPaymentHistory(userId, pageable);
    }



}
