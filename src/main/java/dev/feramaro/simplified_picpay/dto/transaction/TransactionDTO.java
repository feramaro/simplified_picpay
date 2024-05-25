package dev.feramaro.simplified_picpay.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull @DecimalMin("0.01")
        BigDecimal value,
        @NotNull @Min(1)
        Long payer,
        @NotNull @Min(1)
        Long payee) {
}
