package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsOverviewDto {
    private Double totalRevenue;
    private Double totalDiscount;
    private Double totalTax;
    private Double totalServiceCharge;
    private Double avgBillValue;
    private Integer totalBills;
}
