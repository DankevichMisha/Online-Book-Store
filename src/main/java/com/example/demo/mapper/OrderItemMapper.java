package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.OrderItemsResponseDto;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemsResponseDto toDto(OrderItem orderItem);
}
