package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.Category;
import com.devbackend.web_telephone_ttcn.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    // them va cap nhat san pham
    @Override
    public void saveCategory(CategoryDto categoryDto) {
        if(categoryDto.getId() != null){
            //update
            Category categoryOld = categoryRepository.findById(categoryDto.getId()).get();
            categoryOld.setCategoryName(categoryDto.getName());
            categoryOld.setIsDelete(categoryDto.getIsDelete());
            categoryOld.setUpdatedDate(new Date());
            categoryRepository.save(categoryOld);
        }else {
            Category category = new Category();
            category.setCategoryName(categoryDto.getName());
            category.setIsDelete(0);
            category.setCreatedDate(new Date());
            category.setUpdatedDate(new Date());
            categoryRepository.save(category);
        }
    }

    // chi lay ra 5 danh muc de hien thi len trang chu
    @Override
    public List<CategoryDto> findAll() {
        List<Category> list = categoryRepository.getAll(PageRequest.of(0,5));
        return list.stream().map((category) -> maptoDto(category)).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> list = categoryRepository.getAll();
        return list.stream().map((category) -> maptoDto(category)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).get();
        return maptoDto(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public boolean deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).get();
        if(category!=null){
            category.setIsDelete(1);
            category.setUpdatedDate(new Date());
            deleteProductByCategoryId(id);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> list = categoryRepository.searchCategory(keyword);
        return list.stream().map((category) -> maptoDto(category)).collect(Collectors.toList());
    }


    private CategoryDto maptoDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getCategoryName());
        categoryDto.setIsDelete(category.getIsDelete());
        return categoryDto;
    }

    @Override
    public void deleteProductByCategoryId(Long categoryId) {
        categoryRepository.deleteProductByCategoryId(categoryId);
    }
}
