package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private String id;
    private String name;
    private Double price;
    private String billingCycle;
    private String description;
    private List<String> features;
    private Boolean isPopular;
}
