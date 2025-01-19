package com.example.demo.dto.order;

public record OrderItemsResponseDto(Long id,
                                    Long bookId,
                                    int quantity) {
}
