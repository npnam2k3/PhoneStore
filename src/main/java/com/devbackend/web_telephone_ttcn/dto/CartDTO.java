package com.devbackend.web_telephone_ttcn.dto;

import com.devbackend.web_telephone_ttcn.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private int quantity;
    private Integer totalPrice;
    private Product product;

    @Override
    public String toString() {
        return "CartDTO{" +
                "quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", product=" + product +
                '}';
    }
}
