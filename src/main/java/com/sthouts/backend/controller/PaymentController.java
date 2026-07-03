package com.sthouts.backend.controller;

import com.sthouts.backend.dto.CheckoutRequestDto;
import com.sthouts.backend.dto.PaymentResponseDto;
import com.sthouts.backend.dto.PlanDto;
import com.sthouts.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanDto>> getPlans() {
        return ResponseEntity.ok(paymentService.getPlans());
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponseDto> checkout(@RequestBody CheckoutRequestDto request) {
        return ResponseEntity.ok(paymentService.processCheckout(request));
    }
}
