package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
