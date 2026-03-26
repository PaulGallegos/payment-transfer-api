package com.paulgallegos.payment.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentRequest {
    @NotNull
    private UUID receiverAccountId;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
    @Size(max = 255)
    private String description;
    @NotBlank
    private String idempotencyKey;
}
