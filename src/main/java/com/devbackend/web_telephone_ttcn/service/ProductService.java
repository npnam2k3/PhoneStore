package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.ProductDto;
import com.devbackend.web_telephone_ttcn.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
//    void saveProduct(ProductDto productDto);
    void saveProduct(Product product);
//    void saveProduct(ProductDto productDto);
    List<Product> findAll();
    Product findById(Long id);
//    ProductDto getById(Long id);
    void deleteProduct(Long id);

    List<Product> getListProductByCategoryId(Long id);

    //phan trang theo danh muc san pham
    Page<Product> getListProductByCategoryIdAndPageable(Long id, Integer pageNo);

    //phan trang va tim kiem theo danh muc san pham
    Page<Product> getListProductByCategoryIdAndPageableAndSearch(String keyword, Long id, Integer pageNo);

    List<Product> searchProduct(String keyword);

    //phan trang
    Page<Product> getAllPageable(Integer pageNo);

    //phan trang va tim kiem
    Page<Product> searchAndPageable(String keyword, Integer pageNo);

    // cập nhật số lượng sản phẩm sau khi mua hàng
    void updateQuantityProduct(Long idProduct, int quantityPurchased);

    // cập nhật số lượng sản phẩm sau khi khách hủy đơn hàng
    void updateQuantityInStock(Long idProduct, int quantity);

    // loc san pham theo gia cua tung danh muc
    List<Product> filterListProductByPrice(Long rangeStart, Long rangeEnd, Long id);
}
