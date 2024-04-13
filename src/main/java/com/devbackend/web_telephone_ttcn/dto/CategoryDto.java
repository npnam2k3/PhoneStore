package com.devbackend.web_telephone_ttcn.dto;

import com.devbackend.web_telephone_ttcn.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "Tên danh mục không đuược để trống")
    private String name;
    private int isDelete;

    private List<Product> products;
}
