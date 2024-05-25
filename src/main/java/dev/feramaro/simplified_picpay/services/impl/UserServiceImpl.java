package dev.feramaro.simplified_picpay.services.impl;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.domain.user.UserType;
import dev.feramaro.simplified_picpay.dto.user.UserDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.UserCreationException;
import dev.feramaro.simplified_picpay.repositories.UserRepository;
import dev.feramaro.simplified_picpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<User> createNewUser(UserDTO userDTO) {
        if(userRepository.findUserByEmailOrCPF(userDTO.email(), userDTO.cpf()).isPresent()) {
            throw new UserCreationException("User exists on database!");
        }
        tryConvertUserTypeToEnum(userDTO.userType());
        User user = new User();
        user.setUserType(UserType.valueOf(userDTO.userType()));
        user.setCPF(userDTO.cpf());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setFullName(userDTO.fullName());
        user.setBalance(userDTO.balance());
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    private void tryConvertUserTypeToEnum(String userType) {
        try {
            UserType.valueOf(userType);
        } catch (IllegalArgumentException ex) {
            throw new UserCreationException("Invalid userType");
        }
    }
}
