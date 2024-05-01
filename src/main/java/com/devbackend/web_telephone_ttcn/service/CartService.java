package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.CartDTO;
import com.devbackend.web_telephone_ttcn.entity.Cart;
import com.devbackend.web_telephone_ttcn.entity.CartItem;
import com.devbackend.web_telephone_ttcn.entity.Product;

import java.util.List;

public interface CartService {
    void createCart(Long userId);
    boolean checkCartOfUser(Long idUser);

    boolean addCartItemToCart(CartDTO cartDTO, Cart cart);

    Cart findByUserId(Long userId);

    boolean checkCartItemExists(Long idProduct, Long idCart);

    CartItem getCartItemByProductId(Long idProduct, Long idCart);

    // tăng cartItem = quantity + quantity cũ
    boolean updateCartItem(CartItem cartItem, int quantity, Cart cart);
    void updateCartItem(Long idCartItem, int quantity);

    Cart getCartOfUserByUserId(Long userId);

    List<CartItem> getListCartItemByCartId(Long cartId);

    CartItem getCartItemById(Long idCartItem);

    void deleteCartItemById(Long id);
}
