package com.sthouts.backend.controller;

import com.sthouts.backend.dto.*;
import com.sthouts.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsOverviewDto> getOverview(
            @RequestParam(defaultValue = "7d") String range,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getOverview(range, startDate, endDate));
    }

    @GetMapping("/today-vs-yesterday")
    public ResponseEntity<TodayYesterdayDto> getTodayYesterday() {
        return ResponseEntity.ok(analyticsService.getTodayYesterday());
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<List<DailyRevenueDto>> getDailyRevenue(
            @RequestParam(defaultValue = "7d") String range,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getDailyRevenue(range, startDate, endDate));
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<PaymentMethodBreakdownDto> getPaymentBreakdown(
            @RequestParam(defaultValue = "7d") String range,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getPaymentBreakdown(range, startDate, endDate));
    }

    @GetMapping("/top-items")
    public ResponseEntity<List<TopItemDto>> getTopItems(
            @RequestParam(defaultValue = "7d") String range,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String date) {
        if (date != null && !date.trim().isEmpty() && (startDate == null || startDate.trim().isEmpty())) {
            startDate = date;
            endDate = date;
        }
        return ResponseEntity.ok(analyticsService.getTopItems(range, startDate, endDate));
    }
}


