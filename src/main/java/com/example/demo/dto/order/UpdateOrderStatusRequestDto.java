package com.example.demo.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequestDto(@NotBlank(message = "Please, set order status")
                                          Order.Status status) {
}
