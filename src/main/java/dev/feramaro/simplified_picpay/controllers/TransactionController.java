package dev.feramaro.simplified_picpay.controllers;

import dev.feramaro.simplified_picpay.dto.transaction.TransactionDTO;
import dev.feramaro.simplified_picpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.makeTransaction(transactionDTO);
    }
}
