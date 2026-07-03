package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryStructureDto {
    private String staffId;
    private Double basic;
    private Double hra;
    private Double foodAllowance;
    private Double travelAllowance;
    private Double pfDeduction;
    private Double taxDeduction;
}
