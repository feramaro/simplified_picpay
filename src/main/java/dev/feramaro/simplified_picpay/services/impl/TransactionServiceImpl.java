package dev.feramaro.simplified_picpay.services.impl;

import dev.feramaro.simplified_picpay.domain.transaction.Transaction;
import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.domain.user.UserType;
import dev.feramaro.simplified_picpay.dto.transaction.TransactionDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.NotAuthorizedException;
import dev.feramaro.simplified_picpay.infra.exceptions.TransactionException;
import dev.feramaro.simplified_picpay.repositories.TransactionRepository;
import dev.feramaro.simplified_picpay.repositories.UserRepository;
import dev.feramaro.simplified_picpay.services.AuthorizationService;
import dev.feramaro.simplified_picpay.services.NotificationService;
import dev.feramaro.simplified_picpay.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private NotificationService notificationService;


    @Override
    @Transactional
    public ResponseEntity<?> makeTransaction(TransactionDTO transactionDTO) {
        User payer = userRepository.findById(transactionDTO.payer()).orElseThrow(() -> new TransactionException("Payer not found"));
        User payee = userRepository.findById(transactionDTO.payee()).orElseThrow(() -> new TransactionException("Payee not found"));

        if(payer.getUserType() == UserType.STORE) {
            throw new TransactionException("Store can't pay to other users");
        }
        if(payer.getBalance() < transactionDTO.value()) {
            throw new TransactionException("Payer's don't have enough money in the account");
        }

        boolean hasAuthorization = authorizationService.authorize(payee, transactionDTO.value());
        if(hasAuthorization) {
            withdrawAccount(payer, transactionDTO.value());
            depositIntoAccount(payee, transactionDTO.value());
            userRepository.save(payer);
            userRepository.save(payee);
            log.info("Payerrrr:" + payer.getBalance());
            Transaction transaction = new Transaction();
            transaction.setPayee(payee);
            transaction.setPayer(payer);
            transaction.setAmount(transactionDTO.value());
            transactionRepository.save(transaction);
            notificationService.notify(transaction);
        } else {
            throw new NotAuthorizedException("Transaction not authorized!");
        }

        return ResponseEntity.ok().build();
    }

    private void withdrawAccount(User user, Double amount) {
        user.setBalance(user.getBalance()-amount);
    }

    private void depositIntoAccount(User user, Double amount) {
        user.setBalance(user.getBalance()+amount);
    }
}
