package com.demo.item_service.pojo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TailorPojo {

    private Long tailorId;
    private String tailorName;
    private String tailorMobileNumber;
    private Long workload;
    private Boolean isActive = true;  // Default value is true (active)
}

