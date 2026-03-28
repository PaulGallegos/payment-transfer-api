package com.paulgallegos.payment.service;

import com.paulgallegos.payment.domain.Payment;
import com.paulgallegos.payment.domain.PaymentStatus;
import com.paulgallegos.payment.dto.CreatePaymentRequest;
import com.paulgallegos.payment.dto.PaymentResponse;
import com.paulgallegos.payment.exception.DuplicatePaymentException;
import com.paulgallegos.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(UUID senderAccountId, CreatePaymentRequest paymentRequest) {

        // Check if payment exist with idempotencyKey thrown an exception

        paymentRepository.findByIdempotencyKey(paymentRequest.getIdempotencyKey())
                .ifPresent( existing -> {throw new DuplicatePaymentException(paymentRequest.getIdempotencyKey());});


        //create the payment
        Payment payment =
                Payment.builder()
                        .senderAccountId(senderAccountId)
                        .receiverAccountId(paymentRequest.getReceiverAccountId())
                        .amount(paymentRequest.getAmount())
                        .currency(paymentRequest.getCurrency())
                        .status(PaymentStatus.PENDING)
                        .description(paymentRequest.getDescription())
                        .idempotencyKey(paymentRequest.getIdempotencyKey())
                        .build();

         Payment paymentSaved =  paymentRepository.saveAndFlush(payment);

         return PaymentResponse.builder()
                 .id(paymentSaved.getId())
                 .senderAccountId(paymentSaved.getSenderAccountId())
                 .receiverAccountId(paymentSaved.getReceiverAccountId())
                 .amount(paymentSaved.getAmount())
                 .currency(paymentSaved.getCurrency())
                 .status(paymentSaved.getStatus())
                 .description(paymentSaved.getDescription())
                 .createdAt(paymentSaved.getCreatedAt())
                 .build();
    }

}
