package dev.feramaro.simplified_picpay.services.impl;

import dev.feramaro.simplified_picpay.domain.transaction.Transaction;
import dev.feramaro.simplified_picpay.dto.notification.SendNotificationDTO;
import dev.feramaro.simplified_picpay.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private RestClient restClient;

    @Value("${app.notification-url}")
    private String notificationUrl;

    @Override
    public void notify(Transaction transaction) {
        SendNotificationDTO sendNotificationDTO = new SendNotificationDTO(transaction.getPayer().getEmail(), transaction.getPayee().getEmail());
        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri(notificationUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sendNotificationDTO)
                    .retrieve()
                    .toBodilessEntity();
            if(response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info(String.format("Notification sent to transaction %s", transaction.getId()));
            }

        } catch (HttpServerErrorException ex) {
            log.info(String.format("Fail to send notification to transaction %s", transaction.getId()));
        } catch (Exception ex) {
            log.error("Error on call notification service", ex);
        }
    }
}
