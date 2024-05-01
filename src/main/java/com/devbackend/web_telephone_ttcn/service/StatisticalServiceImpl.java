package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.repository.OrderRepository;
import com.devbackend.web_telephone_ttcn.repository.ProductRepository;
import com.devbackend.web_telephone_ttcn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Object[]> getRevenueByMonth(int month, int year) {
        return orderRepository.getRevenueByMonthYear(month, year);
    }

    @Override
    public Long calculateTotalRevenue() {
        return orderRepository.calculateTotalRevenue() != null ?orderRepository.calculateTotalRevenue():0;
    }

    @Override
    public int calculateOrderCanceled() {
        return orderRepository.calculateOrderCanceled();
    }

    @Override
    public int countUser() {
        return userRepository.countUsersByRoleId(1L);
    }

    @Override
    public int countOrder() {
        return orderRepository.countOrderById();
    }

    @Override
    public List<Object[]> getTopSellingProducts() {
        return orderRepository.getTopSellingProducts();
    }

    @Override
    public List<Product> getProductsInventory() {
        return productRepository.getProductsInventory();
    }

    @Override
    public List<Product> getProductsRunningOut() {
        return productRepository.getProductsRunningOut();
    }
}
