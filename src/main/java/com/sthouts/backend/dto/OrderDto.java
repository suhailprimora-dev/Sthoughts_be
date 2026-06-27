package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String billNo;
    private String customerName;
    private String tableNo;
    private Double discount;
    private Double serviceCharge;
    private String paymentMethod;
    private Double gstRate;
    private Double subtotal;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
