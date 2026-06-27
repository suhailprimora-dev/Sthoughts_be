package com.sthouts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayYesterdayDto {
    private Double todayRevenue;
    private Double yesterdayRevenue;
    private Double changePercentage;
}
