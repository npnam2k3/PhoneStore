package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.CartDTO;
import com.devbackend.web_telephone_ttcn.entity.Cart;
import com.devbackend.web_telephone_ttcn.entity.CartItem;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.repository.CartItemRepository;
import com.devbackend.web_telephone_ttcn.repository.CartRepository;
import com.devbackend.web_telephone_ttcn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void createCart(Long userId) {
        Cart cart = new Cart();
        User user = userRepository.findById(userId).get();
        cart.setUser(user);
        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);
    }

    @Override
    public boolean checkCartOfUser(Long idUser) {
        Cart cart = cartRepository.findCartByUserId(idUser);
        if(cart != null){
            return true;
        }
        return false;
    }

    @Override
    public boolean addCartItemToCart(CartDTO cartDTO, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(cartDTO.getQuantity());
        cartItem.setTotalPrice(cartDTO.getTotalPrice());
        cartItem.setProduct(cartDTO.getProduct());
        if(cartItemRepository.save(cartItem) != null){
            return true;
        }
        return false;
    }

    @Override
    public Cart findByUserId(Long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    @Override
    public boolean checkCartItemExists(Long idProduct, Long idCart) {
        if(cartItemRepository.findCartItemByProductId(idProduct, idCart) != null){
            return true;
        }
        return false;
    }

    @Override
    public CartItem getCartItemByProductId(Long idProduct, Long cartId) {
        return cartItemRepository.findCartItemByProductId(idProduct, cartId);
    }

    @Override
    public boolean updateCartItem(CartItem cartItem, int quantity, Cart cart) {
        cartItem.setCart(cart);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice(cartItem.getTotalPrice() + (cartItem.getProduct().getPrice()*quantity));
        if(cartItemRepository.save(cartItem) != null)
            return true;
        return false;
    }

    @Override
    public void updateCartItem(Long idCartItem, int quantity) {
        CartItem cartItem = cartItemRepository.findById(idCartItem).get();
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * (cartItem.getProduct().getPrice()));
        cartItemRepository.save(cartItem);
    }

    @Override
    public Cart getCartOfUserByUserId(Long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    @Override
    public List<CartItem> getListCartItemByCartId(Long cartId) {
        return cartItemRepository.getListCartItemByCartId(cartId);
    }

    @Override
    public CartItem getCartItemById(Long idCartItem) {
        return cartItemRepository.findById(idCartItem).get();
    }

    @Override
    public void deleteCartItemById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
