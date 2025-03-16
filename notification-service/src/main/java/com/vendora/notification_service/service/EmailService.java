package com.vendora.notification_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, Map<String, Object> variables, String template) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(template, context);

        helper.setFrom(mailFrom);  // Отправитель
        helper.setTo(to);  // Получатель
        helper.setSubject(subject);  // Тема письма
        helper.setText(htmlContent, true); // Тело письма

        mailSender.send(message);
    }
}