package dev.feramaro.simplified_picpay.infra.exceptions;


public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
