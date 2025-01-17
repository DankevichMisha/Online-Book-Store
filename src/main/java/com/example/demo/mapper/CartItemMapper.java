package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toEntity(CartItemRequestDto cartItem);
}
