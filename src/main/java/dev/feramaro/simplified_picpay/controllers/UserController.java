package dev.feramaro.simplified_picpay.controllers;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.dto.user.UserDTO;
import dev.feramaro.simplified_picpay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createNewUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.createNewUser(userDTO);
    }
}
