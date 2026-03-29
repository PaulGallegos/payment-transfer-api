package com.paulgallegos.payment.kafka;

import com.paulgallegos.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private UUID paymentId;
    private UUID senderAccountId;
    private UUID receiverAccountId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private LocalDateTime occuredAt;
}
