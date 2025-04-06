package com.vendora.warehouse_service.DTO;

public class SendImportDTO {
    private String userId;
    private String status;

    public SendImportDTO(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public SendImportDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
