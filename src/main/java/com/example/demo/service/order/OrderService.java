package com.example.demo.service.order;

import com.example.demo.dto.order.OrderItemsResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.PlaceOrderRequestDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.model.User;
import java.util.List;

public interface OrderService {
    OrderResponseDto placeOrder(User user, PlaceOrderRequestDto placeOrderRequestDto);

    List<OrderResponseDto> getAllOrders(User user);

    OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);

    OrderItemsResponseDto findOrderItemByIdAndOrderId(Long orderId, Long orderItemId, User user);

    OrderResponseDto findOrderById(Long id, User user);
}
