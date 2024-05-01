package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.dto.ProductDto;
import com.devbackend.web_telephone_ttcn.entity.Category;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.getAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

//    @Override
//    public ProductDto getById(Long id) {
//        return mapToDto(productRepository.findById(id).get());
//    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).get();
        product.setIsDelete(1);
        productRepository.save(product);
    }

    // lay danh sach san pham theo id danh muc
    @Override
    public List<Product> getListProductByCategoryId(Long id) {
        return productRepository.getListProductByCategoryId(id);
    }


    // lay danh sach san pham theo id danh muc va phan trang
    @Override
    public Page<Product> getListProductByCategoryIdAndPageable(Long id, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,3);
        return productRepository.getListProductByCategoryIdAndPageable(id, pageable);
    }

    // lay danh sach san pham theo id danh muc va ket hop phan trang - tim kiem
    @Override
    public Page<Product> getListProductByCategoryIdAndPageableAndSearch(String keyword, Long id, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,3);
        return productRepository.getListProductByCategoryIdAndPageableAndSearch(id, keyword, pageable);
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword);
    }

    // lay tat ca san pham va phan trang
    @Override
    public Page<Product> getAllPageable(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return productRepository.getListProductPageable(pageable);
    }

    // tim kiem va phan trang
    @Override
    public Page<Product> searchAndPageable(String keyword, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return productRepository.searchProductAndPageable(keyword, pageable);
    }

    @Override
    public void updateQuantityProduct(Long idProduct, int quantityPurchased) {
        Product product = productRepository.findById(idProduct).get();
        product.setQuantityInStock(product.getQuantityInStock() - quantityPurchased);
        productRepository.save(product);
    }

    @Override
    public void updateQuantityInStock(Long idProduct, int quantity) {
        Product product = productRepository.findById(idProduct).get();
        product.setQuantityInStock(product.getQuantityInStock() + quantity);
        productRepository.save(product);
    }

}
