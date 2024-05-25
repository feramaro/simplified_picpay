package dev.feramaro.simplified_picpay.repositories;

import dev.feramaro.simplified_picpay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailOrCPF(String email, String CPF);
}
