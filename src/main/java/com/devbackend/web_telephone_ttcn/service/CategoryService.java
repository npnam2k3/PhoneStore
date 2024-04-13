package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    void saveCategory(CategoryDto categoryDto);
    List<CategoryDto> findAll();
    CategoryDto findById(Long id);
    Category getById(Long id);
    boolean deleteCategory(Long id);

    List<CategoryDto> searchCategory(String keyword);

    // xóa các sản phẩm thuộc danh mục khi danh mục đó bị xóa
    void deleteProductByCategoryId(Long categoryId);
}
