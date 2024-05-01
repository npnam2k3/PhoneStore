package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Order;
import com.devbackend.web_telephone_ttcn.repository.OrderRepository;
import com.devbackend.web_telephone_ttcn.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public boolean checkOrderNumberExits(String orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
        if(order != null)
            return true; // co ton tai
        return false;
    }

    @Override
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long idOrder) {
        return orderRepository.findById(idOrder).get();
    }

    @Override
    public List<Order> getListOrderOfCustomer(Long userid) {
        return orderRepository.getListOrder(userid);
    }

    @Override
    public List<Order> getListOrderPurchased(Long userId) {
        return orderRepository.getListOrderPurchased(userId);
    }

    @Override
    public List<Order> getListOrderByOrderStatus(int orderStatus) {
        return orderRepository.getOrderByOrderStatus(orderStatus);
    }

    @Override
    public void updateOrderByOrderStatus(int orderStatus, Long orderId) {
//        orderRepository.updateOrderByOrderStatus(orderStatus, orderId);
        Order order = orderRepository.findById(orderId).get();
        if(orderStatus == Constant.DELIVERY_SUCCESSFULLY_ORDER_STATUS) {
            order.setDeliveredDate(new Date());
            order.setPaymentStatus(Constant.PAYMENT_STATUS_PAID);
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

}
