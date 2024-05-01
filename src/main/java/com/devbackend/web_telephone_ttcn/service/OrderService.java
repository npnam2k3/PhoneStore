package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Order;

import java.util.List;

public interface OrderService {
    boolean checkOrderNumberExits(String orderNumber);
    void createOrder(Order order);

    // lay hoa don theo id
    Order getOrderById(Long idOrder);

    // lấy danh sách đơn hàng của khách hàng với trang thái: chua xac nhan, dang giao
    List<Order> getListOrderOfCustomer(Long userId);

    // lấy danh sách đơn hàng của khách hàng với trang thai: đã giao
    List<Order> getListOrderPurchased(Long userId);

    // lay danh sach hoa don theo trang thai hoa don
    List<Order> getListOrderByOrderStatus(int orderStatus);

    // update hoa don theo trang thai
    void updateOrderByOrderStatus(int orderStatus, Long orderId);
}
