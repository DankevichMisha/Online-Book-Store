package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;

public interface ShoppingCartService {
    void registerNewShoppingCart(User user);

    ShoppingCartResponseDto addBookToShoppingCart(User user, CartItemRequestDto cartItem);

    ShoppingCartResponseDto getShoppingCart(User user);

    ShoppingCartResponseDto updateShoppingCart(Long itemId,
                                               User user,
                                               UpdateCartItemRequestDto cartItemDto);

    ShoppingCartResponseDto removeItemFromShoppingCart(User user, Long itemId);
}
