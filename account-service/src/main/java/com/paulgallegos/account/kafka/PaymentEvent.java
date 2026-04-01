package com.paulgallegos.account.kafka;

import com.paulgallegos.account.domain.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private UUID paymentId;
    private UUID senderAccountId;
    private UUID receiverAccountId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private LocalDateTime occuredAt;
}
