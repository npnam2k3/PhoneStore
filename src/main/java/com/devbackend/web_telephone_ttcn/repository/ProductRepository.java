package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // lấy ra tất cả sản phẩm chưa xóa (isDelete=0)
    @Query("select p from Product p where p.isDelete = 0")
    List<Product> getAll();

    // lấy ra danh sách sản phẩm theo id danh mục
    @Query("select p from Product p where p.category.id = ?1 and p.isDelete = 0")
    List<Product> getListProductByCategoryId(Long id);

    // loc san pham theo gia cua tung danh muc
    @Query("select p from Product p where (p.price between ?1 and ?2) and p.category.id = ?3 and p.isDelete = 0")
    List<Product> filterListProductByPrice(Long rangeStart, Long rangeEnd, Long id);

    // lấy ra danh sách sản phẩm theo id danh mục va phan trang
    @Query("select p from Product p where p.category.id = ?1 and p.isDelete = 0")
    Page<Product> getListProductByCategoryIdAndPageable(Long id, Pageable pageable);

    // lấy ra danh sách sản phẩm theo id danh mục va phan trang va tim kiem
    @Query("select p from Product p where p.category.id = ?1 and p.productName like %?2% and p.isDelete = 0")
    Page<Product> getListProductByCategoryIdAndPageableAndSearch(Long id, String keyword, Pageable pageable);

    // lấy danh sách theo từ khóa tìm kiếm
    @Query("select p from Product p where p.productName like %?1% and p.isDelete=0")
    List<Product> searchProduct(String keyword);

    // phân trang
    @Query("select p from Product p where p.isDelete = 0")
    Page<Product> getListProductPageable(Pageable pageable);

    // phan trang ket hop tim kiem
    @Query("select p from Product p where p.productName like %?1% and p.isDelete=0")
    Page<Product> searchProductAndPageable(String keyword, Pageable pageable);


    // thong ke cac san pham sap het hang
    @Query(value = "select p from Product p where p.isDelete = 0 and p.quantityInStock <= 5")
    List<Product> getProductsRunningOut();

    // thống kê các san pham tồn kho
    @Query(value = "select p from Product p where p.isDelete = 0 and p.quantityInStock > 30")
    List<Product> getProductsInventory();
}
