package dev.feramaro.simplified_picpay.services;

import dev.feramaro.simplified_picpay.domain.transaction.Transaction;

public interface NotificationService {

    void notify(Transaction transaction);
}
