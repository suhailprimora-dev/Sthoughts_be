package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodBreakdownDto {
    private Double cash;
    private Double card;
    private Double upi;
    private Double totalRevenue;
    private Double totalDiscount;
    private Double totalServiceCharge;
}
