package com.example.demo.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(@NotNull(message = "Please, set order status")
                                          Order.Status status) {
}
