package com.devbackend.web_telephone_ttcn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true)
    private String orderNumber; // ma hoa don dung de giao dich

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "payment_status")
    private int paymentStatus;  // trang thai thanh toan: 1-da thanh toan, 2-chua thanh toan

    @Column(name = "order_status")
    private int orderStatus;  // trang thai hoa don: 1-chua xac nhan, 2-da xac nhan, 3 - dang giao, 4-giao thanh cong, 5-da huy

    @Column(name = "total_cost")
    private Integer totalCost; // tong tien don hang

    @Column(name = "delivery_address")
    private String deliveryAddress; // dia chi giao hang

    @Column(name = "receive_mode")
    private int receiveMode; // hinh thuc nhan hang: 1-tai cua hang; 2-giao tai nha

    @Column(name = "delivered_date") // ngay giao hang thanh cong
    private Date deliveredDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
