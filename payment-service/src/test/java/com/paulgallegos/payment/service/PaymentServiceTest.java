package com.paulgallegos.payment.service;

import com.paulgallegos.payment.domain.Payment;
import com.paulgallegos.payment.domain.PaymentStatus;
import com.paulgallegos.payment.dto.CreatePaymentRequest;
import com.paulgallegos.payment.dto.PaymentResponse;
import com.paulgallegos.payment.exception.DuplicatePaymentException;
import com.paulgallegos.payment.kafka.PaymentEventProducer;
import com.paulgallegos.payment.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    private UUID senderId;
    private CreatePaymentRequest request;

    @BeforeEach
    public void setUp(){

        senderId = UUID.randomUUID();

        request = new CreatePaymentRequest();
        request.setReceiverAccountId(UUID.randomUUID());
        request.setAmount(new BigDecimal("1000.00"));
        request.setCurrency("JPY");
        request.setIdempotencyKey("test-key-001");
        request.setDescription("Test payment");

    }

    @Test
    @DisplayName("Should create payment and publish Kafka event")
    public void testCreatePayment(){

        // Arrange
        Payment savedPayment = Payment.builder()
                .id(UUID.randomUUID())
                .senderAccountId(senderId)
                .receiverAccountId(request.getReceiverAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        when(paymentRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(paymentRepository.saveAndFlush(any())).thenReturn(savedPayment);

        // Act
        PaymentResponse response = paymentService.createPayment(senderId, request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(response.getAmount()).isEqualByComparingTo("1000.00");
        assertThat(response.getSenderAccountId()).isEqualTo(senderId);
        verify(paymentEventProducer, times(1)).publishPaymentInitiated(any());

    }

    @Test
    @DisplayName("Should throw DuplicatePaymentException")
    public void testCreatePaymentThenThrowDuplicatePaymentException(){

        // Arrange
        Payment duplicatePayment = Payment.builder()
                .id(UUID.randomUUID())
                .senderAccountId(senderId)
                .receiverAccountId(request.getReceiverAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        when(paymentRepository.findByIdempotencyKey(any())).thenReturn(Optional.of(duplicatePayment));

        assertThatThrownBy(()->paymentService.createPayment(senderId, request)).isInstanceOfAny(DuplicatePaymentException.class);
        verify(paymentRepository, never()).save(any());
        verify(paymentEventProducer, never()).publishPaymentInitiated(any());
    }

}
