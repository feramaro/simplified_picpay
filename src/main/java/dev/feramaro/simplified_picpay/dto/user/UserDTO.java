package dev.feramaro.simplified_picpay.dto.user;

import dev.feramaro.simplified_picpay.domain.user.UserType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record UserDTO(
        @NotNull @NotBlank @Size(max = 100)
        String fullName,
        @CPF
        String cpf,
        @Email
        String email,
        @NotNull @NotBlank @Size(min = 6, max = 30)
        String password,
        @NotNull @NotBlank
        String userType,
        @NotNull @DecimalMin("0.01")
        BigDecimal balance
) {
}
