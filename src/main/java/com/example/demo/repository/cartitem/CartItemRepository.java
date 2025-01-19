package com.example.demo.repository.cartitem;

import com.example.demo.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    void deleteCartItemByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
