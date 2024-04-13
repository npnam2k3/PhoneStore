package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.isDelete=0")
    List<Category> getAll();

    @Query("select c from Category c where c.categoryName like %?1% and c.isDelete=0")
    List<Category> searchCategory(String keyword);

    // xóa các sản phẩm thuộc danh mục khi danh mục đó bị xóa
    @Modifying
    @Query("update Product p set p.isDelete = 1 where p.category.id = ?1")
    void deleteProductByCategoryId(Long categoryId);
}
