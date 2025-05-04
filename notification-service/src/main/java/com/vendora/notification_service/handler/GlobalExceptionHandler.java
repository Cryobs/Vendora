package com.vendora.notification_service.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DATA_INCONSISTENCY_TEMPLATE = "Error: ";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(DATA_INCONSISTENCY_TEMPLATE + e.getMessage());
    }
}

