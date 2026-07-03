package com.sthouts.backend.service;

import com.sthouts.backend.dto.CheckoutRequestDto;
import com.sthouts.backend.dto.PaymentResponseDto;
import com.sthouts.backend.dto.PlanDto;
import com.sthouts.backend.model.PaymentTransaction;
import com.sthouts.backend.model.Tenant;
import com.sthouts.backend.repository.PaymentTransactionRepository;
import com.sthouts.backend.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository transactionRepository;
    private final TenantRepository tenantRepository;

    public List<PlanDto> getPlans() {
        return Arrays.asList(
            PlanDto.builder()
                .id("starter")
                .name("Starter POS")
                .price(29.0)
                .billingCycle("per month")
                .description("Ideal for single shop or cafe launching online.")
                .features(Arrays.asList("1 Terminal License", "Live Subdomain Provisioning", "Menu & Table Management", "Standard Cloud Backup"))
                .isPopular(false)
                .build(),
            PlanDto.builder()
                .id("pro")
                .name("Pro Enterprise")
                .price(79.0)
                .billingCycle("per month")
                .description("Advanced cloud POS with multi-outlet analytics.")
                .features(Arrays.asList("Up to 5 Terminals", "Custom Shop Logo Adaptation", "Real-time P&L Reports", "Staff Attendance & Payroll", "24/7 Priority Support"))
                .isPopular(true)
                .build(),
            PlanDto.builder()
                .id("enterprise")
                .name("Unlimited Cloud")
                .price(199.0)
                .billingCycle("per month")
                .description("For large franchise chains and supermarkets.")
                .features(Arrays.asList("Unlimited Terminals", "Dedicated Thoughtit Subdomain", "Custom API Integrations", "Sub-second Barcode Checkout", "Dedicated Account Manager"))
                .isPopular(false)
                .build()
        );
    }

    public PaymentResponseDto processCheckout(CheckoutRequestDto request) {
        String ref = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        PaymentTransaction tx = PaymentTransaction.builder()
                .tenantId(request.getTenantId())
                .planId(request.getPlanId())
                .planName(request.getPlanName())
                .amount(request.getAmount())
                .currency("USD")
                .gateway(request.getPaymentMethod() != null ? request.getPaymentMethod() : "THOUGHTIT_PAY")
                .transactionRef(ref)
                .status("SUCCESS")
                .build();

        transactionRepository.save(tx);

        if (request.getTenantId() != null) {
            tenantRepository.findById(request.getTenantId()).ifPresent(t -> {
                t.setPlan(request.getPlanName() != null ? request.getPlanName() : "PRO");
                tenantRepository.save(t);
            });
        }

        return PaymentResponseDto.builder()
                .success(true)
                .transactionRef(ref)
                .message("Payment successfully verified. Welcome to Thoughtit Enterprise!")
                .redirectUrl("/billing")
                .build();
    }
}
