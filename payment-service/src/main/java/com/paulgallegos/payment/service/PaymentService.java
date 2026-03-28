package com.paulgallegos.payment.service;

import com.paulgallegos.payment.domain.Payment;
import com.paulgallegos.payment.domain.PaymentStatus;
import com.paulgallegos.payment.dto.CreatePaymentRequest;
import com.paulgallegos.payment.dto.PaymentResponse;
import com.paulgallegos.payment.exception.DuplicatePaymentException;
import com.paulgallegos.payment.exception.PaymentNotFoundException;
import com.paulgallegos.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

         return toResponse(paymentSaved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID requestingUserId, UUID paymentId){

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if(!payment.getSenderAccountId().equals(requestingUserId)){
            throw new PaymentNotFoundException(paymentId);
        }

        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentHistory(UUID senderAccountId, Pageable pageable){

       Page<Payment> paymentPage = paymentRepository.findBySenderAccountId(senderAccountId,pageable);

        return paymentPage.map(this::toResponse);
    }


    private PaymentResponse toResponse(Payment payment){
        return  PaymentResponse.builder()
                .id(payment.getId())
                .senderAccountId(payment.getSenderAccountId())
                .receiverAccountId(payment.getReceiverAccountId())
                .status(payment.getStatus())
                .currency(payment.getCurrency())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .build();
    }

}
