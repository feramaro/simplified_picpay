package dev.feramaro.simplified_picpay.services;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.dto.user.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<User> createNewUser(UserDTO userDTO);
}
