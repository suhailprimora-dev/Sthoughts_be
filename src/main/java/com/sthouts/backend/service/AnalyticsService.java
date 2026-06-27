package com.sthouts.backend.service;

import com.sthouts.backend.dto.*;
import com.sthouts.backend.model.Order;
import com.sthouts.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;

    private List<Order> getFilteredOrders(String range) {
        LocalDateTime cutoff = null;
        if ("7d".equalsIgnoreCase(range)) {
            cutoff = LocalDateTime.now().minusDays(7);
        } else if ("30d".equalsIgnoreCase(range)) {
            cutoff = LocalDateTime.now().minusDays(30);
        }

        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> "SETTLED".equals(o.getStatus()))
                .collect(Collectors.toList());

        if (cutoff != null) {
            LocalDateTime finalCutoff = cutoff;
            orders = orders.stream()
                    .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().isAfter(finalCutoff))
                    .collect(Collectors.toList());
        }
        return orders;
    }

    public AnalyticsOverviewDto getOverview(String range) {
        List<Order> orders = getFilteredOrders(range);
        
        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        double totalDiscount = orders.stream().mapToDouble(o -> o.getDiscount() != null ? o.getDiscount() : 0.0).sum();
        double totalTax = orders.stream().mapToDouble(o -> (o.getSubtotal() != null ? o.getSubtotal() : 0.0) * ((o.getGstRate() != null ? o.getGstRate() : 0.0) / 100.0)).sum();
        double totalServiceCharge = orders.stream().mapToDouble(o -> o.getServiceCharge() != null ? o.getServiceCharge() : 0.0).sum();
        double avgBillValue = orders.isEmpty() ? 0.0 : totalRevenue / orders.size();

        return AnalyticsOverviewDto.builder()
                .totalRevenue(totalRevenue)
                .totalDiscount(totalDiscount)
                .totalTax(totalTax)
                .totalServiceCharge(totalServiceCharge)
                .avgBillValue(avgBillValue)
                .totalBills(orders.size())
                .build();
    }

    public TodayYesterdayDto getTodayYesterday() {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> "SETTLED".equals(o.getStatus()) && o.getCreatedAt() != null)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        double todayRev = orders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().equals(today))
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double yesterdayRev = orders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().equals(yesterday))
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double change = 0.0;
        if (yesterdayRev > 0) {
            change = ((todayRev - yesterdayRev) / yesterdayRev) * 100;
        } else if (todayRev > 0) {
            change = 100.0;
        }

        return TodayYesterdayDto.builder()
                .todayRevenue(todayRev)
                .yesterdayRevenue(yesterdayRev)
                .changePercentage(change)
                .build();
    }

    public List<DailyRevenueDto> getDailyRevenue(String range) {
        List<Order> orders = getFilteredOrders(range);
        
        int days = "30d".equalsIgnoreCase(range) ? 30 : ("all".equalsIgnoreCase(range) ? 30 : 7);
        LocalDate start = LocalDate.now().minusDays(days - 1);
        
        Map<String, Double> dailyMap = new HashMap<>();
        for (int i = 0; i < days; i++) {
            dailyMap.put(start.plusDays(i).toString(), 0.0);
        }

        for (Order order : orders) {
            if (order.getCreatedAt() != null) {
                String dateKey = order.getCreatedAt().toLocalDate().toString();
                if (dailyMap.containsKey(dateKey)) {
                    dailyMap.put(dateKey, dailyMap.get(dateKey) + order.getTotalAmount());
                }
            }
        }

        return dailyMap.entrySet().stream()
                .map(e -> new DailyRevenueDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DailyRevenueDto::getDate))
                .collect(Collectors.toList());
    }

    public PaymentMethodBreakdownDto getPaymentBreakdown(String range) {
        List<Order> orders = getFilteredOrders(range);

        double cash = 0, card = 0, upi = 0;
        for (Order order : orders) {
            String method = order.getPaymentMethod() != null ? order.getPaymentMethod().toLowerCase() : "";
            double amt = order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;
            if (method.contains("cash")) cash += amt;
            else if (method.contains("card")) card += amt;
            else if (method.contains("upi")) upi += amt;
        }

        AnalyticsOverviewDto overview = getOverview(range);

        return PaymentMethodBreakdownDto.builder()
                .cash(cash)
                .card(card)
                .upi(upi)
                .totalRevenue(overview.getTotalRevenue())
                .totalDiscount(overview.getTotalDiscount())
                .totalServiceCharge(overview.getTotalServiceCharge())
                .build();
    }

    public List<TopItemDto> getTopItems(String range) {
        List<Order> orders = getFilteredOrders(range);

        Map<String, TopItemDto> itemMap = new HashMap<>();
        
        orders.forEach(order -> {
            order.getItems().forEach(item -> {
                String name = item.getName();
                int qty = item.getQuantity();
                double rev = item.getPrice() * qty;
                
                TopItemDto dto = itemMap.getOrDefault(name, new TopItemDto(name, 0, 0.0));
                dto.setQty(dto.getQty() + qty);
                dto.setRevenue(dto.getRevenue() + rev);
                itemMap.put(name, dto);
            });
        });

        return itemMap.values().stream()
                .sorted((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()))
                .limit(6)
                .collect(Collectors.toList());
    }
}
