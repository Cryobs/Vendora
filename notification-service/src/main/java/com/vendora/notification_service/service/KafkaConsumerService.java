package com.vendora.notification_service.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendora.notification_service.DTO.OrderDTO;
import com.vendora.notification_service.DTO.SendImportDTO;
import com.vendora.notification_service.feign.UserClient;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
public class KafkaConsumerService {

    private static final Logger log = LogManager.getLogger(KafkaConsumerService.class);
    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private UserClient userClient;

    @KafkaListener(topics = "import-notifications", groupId = "import-notifications-group")
    public void sendImportStatus(Message<String> message) throws JsonProcessingException, MessagingException {
        log.info("Kafka message: {}", message.getPayload());
        SendImportDTO importDTO = objectMapper.readValue(message.getPayload(), SendImportDTO.class);

        ResponseEntity<UserRepresentation> userResponse = userClient.getUserById(importDTO.getUserId());
        if (userResponse.getStatusCode().is2xxSuccessful()) {
            UserRepresentation user = userResponse.getBody();
            String userEmail = user.getEmail();

            if (importDTO.getStatus().equals("Success")){
                String subject = "Import Success";

                Map<String, Object> variables = new HashMap<>();
                variables.put("username", user.getUsername());
                emailService.sendEmail(userEmail, subject, variables, "send-success-import/index");
            } else {
                String subject = "Import Unsuccessful";

                Map<String, Object> variables = new HashMap<>();
                variables.put("username", user.getUsername());
                log.info(Arrays.stream(importDTO.getStatus().split(";")).toList().toString());
                variables.put("errors", Arrays.stream(importDTO.getStatus().split(";")).toList());
                emailService.sendEmail(userEmail, subject, variables, "send-unsuccessful-import/index");
            }
        }
    }

    @KafkaListener(topics = "order-notifications", groupId = "order-notifications-group")
    public void sendOrderStatus(Message<String> message) throws MessagingException, JsonProcessingException {
        OrderDTO order =  objectMapper.readValue(message.getPayload(), OrderDTO.class);

        ResponseEntity<UserRepresentation> userResponse = userClient.getUserById(order.getUserId());
        if (userResponse.getStatusCode().is2xxSuccessful()) {
            UserRepresentation user = userResponse.getBody();
            String userEmail = user.getEmail();

            if (order.getStatus().equals("created")){
                String subject = "Order created";

                Map<String, Object> variables = new HashMap<>();
                variables.put("username", user.getUsername());
                variables.put("orderId", order.getId());
                variables.put("items", order.getItems());
                variables.put("finalPrice", order.getFinalPrice());
                emailService.sendEmail(userEmail, subject, variables, "create-order/index");
            } else {
                String subject = "Order status changed";

                Map<String, Object> variables = new HashMap<>();
                variables.put("username", user.getUsername());
                variables.put("orderId", order.getId());
                variables.put("status", order.getStatus());
                emailService.sendEmail(userEmail, subject, variables, "change-order-status/index");
            }


        } else {
            System.out.println(userResponse);
        }

    }
}
