package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // kiem tra xem cartItem da ton tai trong cart hay chua bang product id
    @Query("select ci from CartItem ci where ci.product.id = ?1 and ci.cart.id = ?2")
    CartItem findCartItemByProductId(Long idProduct, Long idCart);

    @Query("select ci from CartItem ci where ci.cart.id = ?1")
    List<CartItem> getListCartItemByCartId(Long cartId);
}
