package com.example.demo.dto.order;

import com.example.demo.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponseDto(Long id,
                               Long userId,
                               Set<OrderItemsResponseDto> orderItems,
                               LocalDateTime orderDate,
                               BigDecimal totalPrice,
                               Order.Status status) {
}
