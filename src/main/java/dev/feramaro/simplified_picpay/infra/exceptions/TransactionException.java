package dev.feramaro.simplified_picpay.infra.exceptions;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }
}
