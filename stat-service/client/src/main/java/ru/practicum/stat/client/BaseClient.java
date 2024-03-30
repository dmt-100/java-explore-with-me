package ru.practicum.stat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static final Logger logger = LoggerFactory.getLogger(BaseClient.class);

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.getStatusCode().is4xxClientError()) {
            logger.error("Клиентская ошибка: Статус код = {}, Тело ответа = {}", response.getStatusCode(), response.getBody());

            String errorMessage = String.format("Произошла ошибка на стороне клиента. Пожалуйста, проверьте ваш запрос. Статус код: %s", response.getStatusCode());
            return responseBuilder.body(errorMessage);
        }

        if (response.getStatusCode().is5xxServerError()) {
            logger.error("Серверная ошибка: Статус код = {}, Тело ответа = {}", response.getStatusCode(), response.getBody());

            String errorMessage = "На сервере произошла ошибка. Мы работаем над ее устранением. Пожалуйста, попробуйте позже.";
            return responseBuilder.body(errorMessage);
        }

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            String errorResponseBody = new String(e.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode errorResponse = mapper.createObjectNode();
            errorResponse.put("error", errorResponseBody);
            errorResponse.put("status", e.getStatusCode().value());
            errorResponse.put("message", e.getStatusText());

            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}