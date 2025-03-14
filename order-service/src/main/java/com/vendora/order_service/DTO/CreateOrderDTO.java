package com.vendora.order_service.DTO;

public class CreateOrderDTO {

    private String shippingAddress;
    private String region; // EU, US

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public CreateOrderDTO() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
