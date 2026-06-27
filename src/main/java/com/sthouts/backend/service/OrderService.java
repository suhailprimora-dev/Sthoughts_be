package com.sthouts.backend.service;

import com.sthouts.backend.dto.OrderDto;
import com.sthouts.backend.dto.OrderItemDto;
import com.sthouts.backend.dto.SettleOrderRequest;
import com.sthouts.backend.model.Order;
import com.sthouts.backend.model.OrderItem;
import com.sthouts.backend.repository.OrderItemRepository;
import com.sthouts.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderDto getActiveOrderOrCreate() {
        Optional<Order> activeOrderOpt = orderRepository.findByStatus("ACTIVE");
        if (activeOrderOpt.isPresent()) {
            return mapToDto(activeOrderOpt.get());
        }

        Order newOrder = Order.builder()
                .billNo("B" + (int)(1000 + Math.random() * 9000))
                .status("ACTIVE")
                .gstRate(5.0) // default 5% based on UI screenshot
                .discount(0.0)
                .serviceCharge(0.0)
                .subtotal(0.0)
                .totalAmount(0.0)
                .createdAt(LocalDateTime.now())
                .build();
        
        return mapToDto(orderRepository.save(newOrder));
    }

    @Transactional
    public OrderDto addItemToOrder(Long orderId, OrderItemDto itemDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(i -> i.getMenuItemId().equals(itemDto.getMenuItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + itemDto.getQuantity());
        } else {
            OrderItem newItem = OrderItem.builder()
                    .order(order)
                    .menuItemId(itemDto.getMenuItemId())
                    .name(itemDto.getName())
                    .price(itemDto.getPrice())
                    .quantity(itemDto.getQuantity())
                    .build();
            order.getItems().add(newItem);
        }

        recalculateTotals(order);
        return mapToDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto updateItemQuantity(Long orderId, Long itemId, int delta) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setQuantity(item.getQuantity() + delta);
        if (item.getQuantity() <= 0) {
            order.getItems().remove(item);
            orderItemRepository.delete(item);
        }

        recalculateTotals(order);
        return mapToDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        order.getItems().remove(item);
        orderItemRepository.delete(item);

        recalculateTotals(order);
        return mapToDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto updateGstRate(Long orderId, Double gstRate) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.setGstRate(gstRate);
        recalculateTotals(order);
        return mapToDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto settleOrder(Long orderId, SettleOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.setCustomerName(request.getCustomerName() != null ? request.getCustomerName() : "Walk-in Customer");
        order.setTableNo(request.getTableNo());
        order.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        order.setServiceCharge(request.getServiceCharge() != null ? request.getServiceCharge() : 0.0);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus("SETTLED");
        
        recalculateTotals(order);
        return mapToDto(orderRepository.save(order));
    }

    public List<OrderDto> getSettledOrders() {
        return orderRepository.findAll().stream()
                .filter(o -> "SETTLED".equals(o.getStatus()))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelActiveOrder() {
        Optional<Order> activeOrderOpt = orderRepository.findByStatus("ACTIVE");
        activeOrderOpt.ifPresent(orderRepository::delete);
    }

    private void recalculateTotals(Order order) {
        double subtotal = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        order.setSubtotal(subtotal);
        double tax = subtotal * ((order.getGstRate() != null ? order.getGstRate() : 0.0) / 100.0);
        double discount = order.getDiscount() != null ? order.getDiscount() : 0.0;
        double serviceCharge = order.getServiceCharge() != null ? order.getServiceCharge() : 0.0;
        
        order.setTotalAmount(subtotal - discount + serviceCharge + tax);
    }

    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(i -> OrderItemDto.builder()
                        .id(i.getId())
                        .menuItemId(i.getMenuItemId())
                        .name(i.getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .billNo(order.getBillNo())
                .customerName(order.getCustomerName())
                .tableNo(order.getTableNo())
                .discount(order.getDiscount())
                .serviceCharge(order.getServiceCharge())
                .paymentMethod(order.getPaymentMethod())
                .gstRate(order.getGstRate())
                .subtotal(order.getSubtotal())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}
