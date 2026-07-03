package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRecordDto {
    private String id;
    private String staffId;
    private String month;
    private Double netSalary;
    private String status;
    private String paidAt;
}
