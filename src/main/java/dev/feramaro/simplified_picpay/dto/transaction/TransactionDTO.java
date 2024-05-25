package dev.feramaro.simplified_picpay.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionDTO(
        @NotNull @Min(1)
        Double value,
        @NotNull @Min(1)
        Long payer,
        @NotNull @Min(1)
        Long payee) {
}
