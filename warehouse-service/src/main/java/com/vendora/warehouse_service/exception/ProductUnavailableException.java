package com.vendora.warehouse_service.exception;

public class ProductUnavailableException extends Exception {
    public ProductUnavailableException(String message) {
        super(message);
    }
}
