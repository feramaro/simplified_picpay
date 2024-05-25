package dev.feramaro.simplified_picpay.services.impl;

import dev.feramaro.simplified_picpay.domain.user.User;
import dev.feramaro.simplified_picpay.dto.authorization.AuthorizationDTO;
import dev.feramaro.simplified_picpay.infra.exceptions.NotAuthorizedException;
import dev.feramaro.simplified_picpay.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Map;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private RestClient restClient;

    @Value("${app.authorization-url}")
    private String authorizationUrl;

    @Override
    public boolean authorize(User user, Double amount) {
        try {
            ResponseEntity<AuthorizationDTO> authorization = restClient
                    .get()
                    .uri(authorizationUrl)
                    .retrieve()
                    .toEntity(AuthorizationDTO.class);

            if (authorization.getStatusCode() == HttpStatus.OK) {
                return authorization.getBody().data().authorization();
            }
        } catch (HttpClientErrorException.Forbidden forbiddenEx) {
            log.info(String.format("User with id %d not authorized make transaction of amount R$ %f", user.getId(), amount));
        } catch (Exception ex) {
            log.error("Error on call authorization service", ex);
            throw new NotAuthorizedException("Error on authorization service, try again later!");
        }
        return false;
    }
}
