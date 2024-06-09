package dev.feramaro.simplified_picpay.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.domain.user.UserType;
import dev.feramaro.simplified_picpay.dto.user.UserDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.UserCreationException;
import dev.feramaro.simplified_picpay.services.impl.UserServiceImpl;

@SpringBootTest
public class UserControllerTest {

    public static final long ID = 1;
    public static final String FULL_NAME = "Fulano de tal";
    public static final String CPF = "37345799026";
    public static final String EMAIL = "foo@bar.com";
    public static final String PASSWORD = "SENHAMUUUITODIFICIL";
    public static final UserType USER_TYPE = UserType.COMMON;
    public static final BigDecimal BALANCE = new BigDecimal("1000.00");



    private AutoCloseable closeable;

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;


    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        startUser();
    }

    @AfterEach
    void close() throws Exception {
        closeable.close();
    }

    @SuppressWarnings("null")
    @Test
    void whenCreateUserThenSuccess() {
        when(userService.createNewUser(userDTO)).thenReturn(new ResponseEntity<User>(user, HttpStatusCode.valueOf(201)));
        ResponseEntity<User> response = userController.createNewUser(userDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(User.class, response.getBody().getClass());
    }

    @Test
    void whenCreateUserThenError() {
        when(userService.createNewUser(userDTO)).thenThrow(new UserCreationException("User exists on database!"));

        UserCreationException thrown = assertThrows(UserCreationException.class, () -> {
           userController.createNewUser(userDTO);
        });
        assertEquals(thrown.getMessage(), "User exists on database!");
    }

    private void startUser() {
        this.userDTO = new UserDTO(FULL_NAME, CPF, EMAIL, PASSWORD, USER_TYPE.toString(), BALANCE);
        this.user = new User(ID, USER_TYPE, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
    }
}
