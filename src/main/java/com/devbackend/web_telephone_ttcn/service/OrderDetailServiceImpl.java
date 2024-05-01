package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.OrderDetail;
import com.devbackend.web_telephone_ttcn.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public void createOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
