package com.paulgallegos.account.dto;

import com.paulgallegos.account.domain.AccountStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AccountResponse {
    private UUID id;
    private UUID userId;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
}
