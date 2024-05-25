package dev.feramaro.simplified_picpay.infra.exceptions;

import dev.feramaro.simplified_picpay.dto.errors.ErrorDTO;
import dev.feramaro.simplified_picpay.dto.errors.FieldErrorDTO;
import dev.feramaro.simplified_picpay.dto.errors.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<FieldErrorDTO> errors = fieldErrors
                .stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ValidationErrorDTO("Validation error", new Date().getTime(), errors);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDTO handleNotAuthorizedException(NotAuthorizedException ex) {
        return new ErrorDTO(ex.getMessage(), new Date().getTime());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserCreationException.class)
    public ErrorDTO handleUserCreationException(UserCreationException ex) {
        return new ErrorDTO(ex.getMessage(), new Date().getTime());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionException.class)
    public ErrorDTO handleTransactionException(TransactionException ex) {
        return new ErrorDTO(ex.getMessage(), new Date().getTime());
    }
}
