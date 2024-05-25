package dev.feramaro.simplified_picpay.infra.exceptions;


public class UserCreationException extends RuntimeException {

    public UserCreationException(String message) {
        super(message);
    }
}
