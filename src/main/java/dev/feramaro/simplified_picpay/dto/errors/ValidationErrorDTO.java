package dev.feramaro.simplified_picpay.dto.errors;

import java.util.List;

public record ValidationErrorDTO(String error, Long timestamp, List<FieldErrorDTO> fieldErrors) {
}
