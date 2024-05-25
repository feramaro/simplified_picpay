package dev.feramaro.simplified_picpay.services;

import dev.feramaro.simplified_picpay.dto.transaction.TransactionDTO;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    ResponseEntity<?> makeTransaction(TransactionDTO transactionDTO);
}
