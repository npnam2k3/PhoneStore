package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.id = ?1")
    Cart findCartByUserId(Long id);

}
