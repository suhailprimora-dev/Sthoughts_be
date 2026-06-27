package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettleOrderRequest {
    private String customerName;
    private String tableNo;
    private Double discount;
    private Double serviceCharge;
    private String paymentMethod;
}
