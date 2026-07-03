package com.sthouts.backend.controller;

import com.sthouts.backend.dto.PublicStatsDto;
import com.sthouts.backend.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class WebsiteController {

    private final TenantRepository tenantRepository;

    @GetMapping("/stats")
    public ResponseEntity<PublicStatsDto> getStats() {
        long shopsCount = tenantRepository.count() + 1420; // Base baseline + registered shops
        PublicStatsDto stats = PublicStatsDto.builder()
                .activeShopsCount(shopsCount)
                .dailyTransactions("145K+")
                .uptime("99.999%")
                .cloudSpeed("18ms")
                .build();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/demo-request")
    public ResponseEntity<Map<String, Object>> requestDemo(@RequestBody Map<String, String> payload) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Demo session scheduled for " + payload.getOrDefault("shopName", "your enterprise") + "!");
        return ResponseEntity.ok(res);
    }
}
