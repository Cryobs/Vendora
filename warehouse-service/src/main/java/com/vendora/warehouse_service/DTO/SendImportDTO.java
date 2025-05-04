package com.vendora.warehouse_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendImportDTO {
    private String userId;
    private String status;
}
