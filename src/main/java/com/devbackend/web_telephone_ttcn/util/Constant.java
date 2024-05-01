package com.devbackend.web_telephone_ttcn.util;

public class Constant {
    public static final int RECEIVE_MODE_AT_STORE = 1;
    public static final int RECEIVE_MODE_AT_HOME = 2;

    public static final int PAYMENT_STATUS_PAID = 1; // đã thanh toán
    public static final int PAYMENT_STATUS_UNPAID = 2; // chưa thanh toán

    public static final int WAITING_CONFIRM_ORDER_STATUS = 1; // chưa xác nhận đơn/ chờ xác nhận
    public static final int  CONFIRMED_ORDER_STATUS = 2; // đã xác nhận
    public static final int DELIVERING_ORDER_STATUS = 3; // dang giao hàng
    public static final int DELIVERY_SUCCESSFULLY_ORDER_STATUS = 4; // giao thành công
    public static final int CANCELED_ORDER_STATUS = 5; // khách trả hàng
}
