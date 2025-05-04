package com.vendora.warehouse_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendora.warehouse_service.DTO.SendImportDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger log = LogManager.getLogger(KafkaProducerService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public CompletableFuture<Void> sendNotification(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        return future
                .thenAccept(result -> handleSuccess(result, message)) // успешная отправка
                .exceptionally(ex -> {
                    handleFailure(ex, message);
                    return null;
                });
    }

    private void handleSuccess(SendResult<String, String> result, String message) {
        System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
    }

    private void handleFailure(Throwable ex, String message) {
        System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
    }

    public void sendImport(Jwt jwt, String status) {
        try {
            String importJson = objectMapper.writeValueAsString(new SendImportDTO(jwt.getSubject(), status));

            this.kafkaTemplate.send("import-notifications", importJson);
        } catch (Exception e) {
            log.error("Kafka send error: {}", e.getMessage(), e);
        }
    }
}