package com.vendora.notification_service.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendora.notification_service.DTO.OrderDTO;
import com.vendora.notification_service.feign.UserClient;
import jakarta.mail.MessagingException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class KafkaConsumerService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private UserClient userClient;

    @KafkaListener(topics = "order-notifications", groupId = "order-notifications-group")
    public void sendOrderStatus(Message<String> message) throws MessagingException, JsonProcessingException {
        OrderDTO order =  objectMapper.readValue(message.getPayload(), OrderDTO.class);

        ResponseEntity<UserRepresentation> userResponse = userClient.getUserById(order.getUserId());
        if (userResponse.getStatusCode().is2xxSuccessful()) {
            UserRepresentation user = userResponse.getBody();
            String userEmail = user.getEmail();

            String subject = "Order status changed";

            Map<String, Object> variables = new HashMap<>();
            variables.put("username", user.getUsername());
            variables.put("status", order.getStatus());
            emailService.sendEmail(userEmail, subject, variables, "email-template");
        } else {
            System.out.println(userResponse);
        }

    }
}
