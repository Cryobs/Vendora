package com.vendora.price_service.exception;

public class UndefinedPromoCodeException extends RuntimeException{
    public UndefinedPromoCodeException(String message) {
        super(message);
    }
}
