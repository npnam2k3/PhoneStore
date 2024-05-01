package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.entity.CartItem;
import com.devbackend.web_telephone_ttcn.entity.Order;
import com.devbackend.web_telephone_ttcn.entity.OrderDetail;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.*;
import com.devbackend.web_telephone_ttcn.util.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;


    // luon luon duoc goi dau tien cho tat ca cac phuong thuc trong Controller
    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/orderOfCustomer")
    public String orderOfCustomer(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        User user = userService.findUserByUsername(username);
        List<Order> orderList = orderService.getListOrderOfCustomer(user.getId());
        if(orderList == null || orderList.isEmpty()){
            model.addAttribute("orderEmpty", "Không có đơn hàng");
        }else {
            model.addAttribute("listOrder", orderList);
        }
        return "user/listOrderOfCustomer";
    }

    @PostMapping("/confirmInforOrder")
    public String confirmInforOrder(@RequestParam("fullname") String fullname,
                               @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("address") String address,
                               @RequestParam("receiveMode") String receiveModeStr,
                               Model model, HttpSession session){
        int receiveMode = -1;
        try {
            receiveMode = Integer.parseInt(receiveModeStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        // lay tong tien
        int totalCheckout = (int) session.getAttribute("totalCheckout");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);

        // tao hoa don
        Order order = new Order();
        order.setOrderNumber(generateUniqueOrderNumber());
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setPaymentStatus(Constant.PAYMENT_STATUS_UNPAID);
        order.setOrderStatus(Constant.WAITING_CONFIRM_ORDER_STATUS);
        order.setTotalCost(totalCheckout);
        if(receiveMode == Constant.RECEIVE_MODE_AT_HOME)
            order.setDeliveryAddress(address);
        order.setReceiveMode(receiveMode);

        // luu hoa don vao database
        orderService.createOrder(order);

        // lay thong tin cac mat hang trong hoa don
        List<CartItem> list = (List<CartItem>) session.getAttribute("listItem");

        List<OrderDetail> orderDetails = new ArrayList<>();
        // tao chi tiet hoa don
        for(CartItem item: list){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(item.getProduct());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setTotalPrice(item.getTotalPrice());
            orderDetail.setOrder(order);

            // them vao danh sach de dua sang trang chi tiet don hang
            orderDetails.add(orderDetail);

            //Cập nhật lai số lượng trong kho sau khi mua
            productService.updateQuantityProduct(item.getProduct().getId(), item.getQuantity());
            // luu vao database
            orderDetailService.createOrderDetail(orderDetail);
        }

        // xoa session list va totalCheckout
        session.removeAttribute("totalCheckout");
        session.removeAttribute("listItem");

        model.addAttribute("order", order);
        model.addAttribute("orderDetailList", orderDetails);
        return "user/orderOfUser";
    }

    @GetMapping("/historyPurchased")
    public String historyPurchased(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        User user = userService.findUserByUsername(username);
        List<Order> orderListPurchased = orderService.getListOrderPurchased(user.getId());
        if(orderListPurchased == null || orderListPurchased.isEmpty()){
            model.addAttribute("orderEmpty", " Bạn chưa có đơn hàng nào đã mua ");
        }else {
            model.addAttribute("orderList", orderListPurchased);
        }
        return "user/historyPurchased";
    }


    // huy don tu khach hang
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") String orderIdStr){
        Long orderId = -1L;
        try {
            orderId = Long.parseLong(orderIdStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        // lay don hang
        Order order = orderService.getOrderById(orderId);

        // cap nhat lai so luong cua tung san pham trong hoa don bi huy
        for(OrderDetail orderDetail: order.getOrderDetails()){
            productService.updateQuantityInStock(orderDetail.getProduct().getId(), orderDetail.getQuantity());
        }
        orderService.updateOrderByOrderStatus(Constant.CANCELED_ORDER_STATUS, orderId);
        return "redirect:/order/orderOfCustomer";
    }

    // Hàm sinh ra một chuỗi ngẫu nhiên có độ dài là ORDER_NUMBER_LENGTH
    private String generateRandomOrderNumber() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int ORDER_NUMBER_LENGTH = 10; // Độ dài của mã hóa đơn
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ORDER_NUMBER_LENGTH; i++) {
            char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }

    // Hàm kiểm tra xem chuỗi đã tồn tại trong cơ sở dữ liệu chưa
    private boolean isOrderNumberExists(String orderNumber) {
        if(orderService.checkOrderNumberExits(orderNumber)){
            return true; // ton tai
        }
        return false;
    }

    // Hàm chính để tạo order number không trùng lặp
    public String generateUniqueOrderNumber() {
        String orderNumber = generateRandomOrderNumber();
        while (isOrderNumberExists(orderNumber)) {
            orderNumber = generateRandomOrderNumber();
        }
        return orderNumber;
    }
}
