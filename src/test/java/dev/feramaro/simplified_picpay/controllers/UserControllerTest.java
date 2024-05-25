package dev.feramaro.simplified_picpay.controllers;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.domain.user.UserType;
import dev.feramaro.simplified_picpay.dto.errors.ErrorDTO;
import dev.feramaro.simplified_picpay.dto.user.UserDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.UserCreationException;
import dev.feramaro.simplified_picpay.repositories.UserRepository;
import dev.feramaro.simplified_picpay.services.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        try {
            userController.createNewUser(userDTO);
            fail("Expected UserCreationException");
        } catch (UserCreationException ex) {
            assertEquals(ex.getClass(), UserCreationException.class);
            assertEquals(ex.getMessage(), "User exists on database!");
        }
    }

    private void startUser() {
        this.userDTO = new UserDTO(FULL_NAME, CPF, EMAIL, PASSWORD, USER_TYPE.toString(), BALANCE);
        this.user = new User(ID, USER_TYPE, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
    }
}
