package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Product;

import java.util.List;
import java.util.Map;

public interface StatisticalService {

    // tinh tong doanh thu theo thang
    List<Object[]> getRevenueByMonth(int month, int year);

    // tinh tong doanh thu cua hang
    Long calculateTotalRevenue();

    // lay so don hang bi huy
    int calculateOrderCanceled();

    // dem tong so luong user trong he thong
    int countUser();

    // dem tong so don hang
    int countOrder();

    // thống kê 5 sản phẩm bán chạy nhất
    List<Object[]> getTopSellingProducts();

    // thống kê sản phẩm tồn kho
    List<Product> getProductsInventory();

    // thống kê san pham sap het hang
    List<Product> getProductsRunningOut();
}
