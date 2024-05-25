package dev.feramaro.simplified_picpay.services;

import dev.feramaro.simplified_picpay.domain.user.User;

public interface AuthorizationService {

    boolean authorize(User user, Double amount);
}
