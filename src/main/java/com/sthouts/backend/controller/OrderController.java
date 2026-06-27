package com.sthouts.backend.controller;

import com.sthouts.backend.dto.OrderDto;
import com.sthouts.backend.dto.OrderItemDto;
import com.sthouts.backend.dto.SettleOrderRequest;
import com.sthouts.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/active")
    public ResponseEntity<OrderDto> getActiveOrderOrCreate() {
        return ResponseEntity.ok(orderService.getActiveOrderOrCreate());
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderDto>> getOrderHistory() {
        return ResponseEntity.ok(orderService.getSettledOrders());
    }

    @DeleteMapping("/active")
    public ResponseEntity<Void> cancelActiveOrder() {
        orderService.cancelActiveOrder();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDto> addItemToOrder(@PathVariable Long orderId, @RequestBody OrderItemDto itemDto) {
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, itemDto));
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDto> updateItemQuantity(@PathVariable Long orderId, @PathVariable Long itemId, @RequestParam int delta) {
        return ResponseEntity.ok(orderService.updateItemQuantity(orderId, itemId, delta));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDto> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.removeItemFromOrder(orderId, itemId));
    }

    @PutMapping("/{orderId}/gst")
    public ResponseEntity<OrderDto> updateGstRate(@PathVariable Long orderId, @RequestParam Double rate) {
        return ResponseEntity.ok(orderService.updateGstRate(orderId, rate));
    }

    @PostMapping("/{orderId}/settle")
    public ResponseEntity<OrderDto> settleOrder(@PathVariable Long orderId, @RequestBody SettleOrderRequest request) {
        return ResponseEntity.ok(orderService.settleOrder(orderId, request));
    }
}
