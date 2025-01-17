package com.example.demo.dto.shoppingcart;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import java.util.Set;

public record ShoppingCartResponseDto(Long userId,
                                      Set<CartItemResponseDto> cartItems) {
}
