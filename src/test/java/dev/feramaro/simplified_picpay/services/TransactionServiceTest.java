package dev.feramaro.simplified_picpay.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.domain.user.UserType;
import dev.feramaro.simplified_picpay.dto.transaction.TransactionDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.TransactionException;
import dev.feramaro.simplified_picpay.repositories.TransactionRepository;
import dev.feramaro.simplified_picpay.repositories.UserRepository;
import dev.feramaro.simplified_picpay.services.impl.AuthorizationServiceImpl;
import dev.feramaro.simplified_picpay.services.impl.TransactionServiceImpl;
import dev.feramaro.simplified_picpay.services.impl.UserServiceImpl;

@SpringBootTest
public class TransactionServiceTest {

    private AutoCloseable closeable;

    @Mock
    private UserServiceImpl userService;
    @Mock
    private AuthorizationServiceImpl authorizationService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User userSender;
    private User userReceiver;
    private User userStore;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        startUsers();
    }

    @AfterEach
    void close() throws Exception {
        closeable.close();
    }

    @Test
    public void whenCreateTransactionSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userSender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userReceiver));
        when(authorizationService.authorize(any(), any())).thenReturn(true);

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10.00"), 1L, 2L);
        transactionService.makeTransaction(transactionDTO);
        User payer = userRepository.findById(1L).get();
        User payee = userRepository.findById(2L).get();

        assertEquals(new BigDecimal("190.00"), payer.getBalance());
        assertEquals(transactionDTO.value(), payee.getBalance());
        verify(userRepository, times(2)).save(any());
        verify(transactionRepository, times(1)).save(any());

    }

    @Test
    public void whenCreateTransactionButPayerNotHaveEnoughMoney() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userSender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userReceiver));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("3000.00"), 1L, 2L);
        User payer = userRepository.findById(1L).get();
        User payee = userRepository.findById(2L).get();

        TransactionException thrown = assertThrows(TransactionException.class, () -> {
            transactionService.makeTransaction(transactionDTO);
        });
        verify(userRepository, times(0)).save(any());
        assertEquals(200, payer.getBalance().doubleValue());
        assertEquals(BigDecimal.ZERO, payee.getBalance());
        assertEquals(thrown.getMessage(), "Payer's don't have enough money in the account");
    }

    @Test
    public void whenCreateTransactionButPayerIsStore() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(userStore));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userSender));

        User payer = userRepository.findById(3L).get();
        User payee = userRepository.findById(1L).get();
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("100.00"), 3L, 1L);

        TransactionException thrown = assertThrows(TransactionException.class, () -> {
           transactionService.makeTransaction(transactionDTO);
        });
        verify(userRepository, times(0)).save(any());
        assertEquals(30000, payer.getBalance().doubleValue());
        assertEquals(200, payee.getBalance().doubleValue());
        assertEquals(thrown.getMessage(), "Store can't pay to other users");
    }

    private void startUsers() {
        userSender = new User(1L, UserType.COMMON, "Fulano de tal", "11111111111", "fulano@mail.com", "senhadificil", new BigDecimal("200.00"));
        userReceiver = new User(2L, UserType.COMMON, "Ciclano da silva", "22222222222", "ciclano@mail.com", "senhadificil", new BigDecimal("0"));
        userStore = new User(3L, UserType.STORE, "Loja FC", "33333333333", "fc@mail.com", "senhadificil", new BigDecimal("30000.00"));

    }
}
