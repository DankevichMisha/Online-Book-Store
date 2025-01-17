package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToShoppingCart(User user, CartItemRequestDto cartItem) {
        Book bookFromDb = bookRepository.findById(cartItem.bookId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + cartItem.bookId()));
        ShoppingCart shoppingCart = findShoppingCart(user.getId());
        shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItem.bookId()))
                .findFirst()
                .ifPresentOrElse(item ->
                                item.setQuantity(item.getQuantity() + cartItem.quantity()),
                        () ->
                                addCartItem(shoppingCart, bookFromDb, cartItem.quantity()));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart(User user) {
        return shoppingCartMapper.toDto(findShoppingCart(user.getId()));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateShoppingCart(Long itemId, User user,
                                                      UpdateCartItemRequestDto cartItemDto) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find item by id " + itemId));
        cartItem.setQuantity(cartItemDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(findShoppingCart(user.getId()));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto removeItemFromShoppingCart(User user, Long itemId) {
        cartItemRepository.deleteCartItemByIdAndShoppingCartId(itemId, user.getId());
        return shoppingCartMapper.toDto(findShoppingCart(user.getId()));
    }

    public void addCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        shoppingCart.getCartItems().add(cartItem);
    }

    private ShoppingCart findShoppingCart(Long id) {
        return shoppingCartRepository.findByUserIdFetchCartItemsAndBooks(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart by id " + id));
    }
}
