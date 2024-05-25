package dev.feramaro.simplified_picpay.services;

import dev.feramaro.simplified_picpay.domain.user.User;

import java.math.BigDecimal;

public interface AuthorizationService {

    boolean authorize(User user, BigDecimal amount);
}
