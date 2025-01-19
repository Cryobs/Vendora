package com.vendora.price_service.exception;

public class NoDiscountException extends RuntimeException {
    public NoDiscountException(String message) {
        super(message);
    }
}