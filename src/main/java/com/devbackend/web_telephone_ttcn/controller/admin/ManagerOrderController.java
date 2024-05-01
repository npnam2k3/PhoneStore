package com.devbackend.web_telephone_ttcn.controller.admin;

import com.devbackend.web_telephone_ttcn.entity.Order;
import com.devbackend.web_telephone_ttcn.entity.OrderDetail;
import com.devbackend.web_telephone_ttcn.service.OrderService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import com.devbackend.web_telephone_ttcn.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class ManagerOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/order/waitingConfirm")
    public String waitingDelivery(Model model){
        List<Order> orderList = orderService.getListOrderByOrderStatus(Constant.WAITING_CONFIRM_ORDER_STATUS);
        if(orderList.isEmpty()){
            model.addAttribute("orderListEmpty", "Không có đơn hàng nào chưa xác nhận ");
        }else{
            model.addAttribute("orderList", orderList);
        }
        return "admin/order/waitingConfirmOrder";
    }

    @GetMapping("/order/confirmed")
    public String confirmedOrder(Model model){
        List<Order> orderList = orderService.getListOrderByOrderStatus(Constant.CONFIRMED_ORDER_STATUS);
        if(orderList.isEmpty()){
            model.addAttribute("orderListEmpty"," Không có đơn hàng nào đã xác nhận ");
        }else{
            model.addAttribute("orderList", orderList);
        }
        return "admin/order/confirmedOrder";
    }

    @GetMapping("/order/delivering")
    public String delivering(Model model){
        List<Order> orderList = orderService.getListOrderByOrderStatus(Constant.DELIVERING_ORDER_STATUS);
        if(orderList.isEmpty()){
            model.addAttribute("orderListEmpty"," Không có đơn hàng nào đang giao ");
        }else{
            model.addAttribute("orderList", orderList);
        }
        return "admin/order/deliveringOrder";
    }

    @GetMapping("/order/canceled")
    public String canceled(Model model){
        List<Order> orderList = orderService.getListOrderByOrderStatus(Constant.CANCELED_ORDER_STATUS);
        if(orderList.isEmpty()){
            model.addAttribute("orderListEmpty"," Không có đơn hàng nào đã hủy ");
        }else{
            model.addAttribute("orderList", orderList);
        }
        return "admin/order/canceledOrder";
    }

    @GetMapping("/order/successfullyDelivery")
    public String successfullyDelivery(Model model){
        List<Order> orderList = orderService.getListOrderByOrderStatus(Constant.DELIVERY_SUCCESSFULLY_ORDER_STATUS);
        if(orderList.isEmpty()){
            model.addAttribute("orderListEmpty"," Không có đơn hàng nào đã giao ");
        }else{
            model.addAttribute("orderList", orderList);
        }
        return "admin/order/successfullyDeliveryOrder";
    }

    @PostMapping("/order/confirmOrder")
    public String confirmOrder(@RequestParam("orderId") String orderIdStr){
        Long orderId = -1L;
        try {
            orderId = Long.parseLong(orderIdStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        orderService.updateOrderByOrderStatus(Constant.CONFIRMED_ORDER_STATUS, orderId);
        return "redirect:/admin/order/confirmed";
    }

    @PostMapping("/order/deliveringOrder")
    public String deliveringOrder(@RequestParam("orderId") String orderIdStr){
        Long orderId = -1L;
        try {
            orderId = Long.parseLong(orderIdStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        orderService.updateOrderByOrderStatus(Constant.DELIVERING_ORDER_STATUS, orderId);
        return "redirect:/admin/order/delivering";
    }

    @PostMapping("/order/successfullyDeliveryOrder")
    public String successfullyDeliveryOrder(@RequestParam("orderId") String orderIdStr){
        Long orderId = -1L;
        try {
            orderId = Long.parseLong(orderIdStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        orderService.updateOrderByOrderStatus(Constant.DELIVERY_SUCCESSFULLY_ORDER_STATUS ,orderId);
        return "redirect:/admin/order/successfullyDelivery";
    }

    @PostMapping("/order/canceledOrder")
    public String canceledOrder(@RequestParam("orderId") String orderIdStr){
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
        orderService.updateOrderByOrderStatus(Constant.CANCELED_ORDER_STATUS ,orderId);
        return "redirect:/admin/order/canceled";
    }

    @GetMapping("/order/detail/{id}")
    public String detailOrder(@PathVariable("id") Long idOrder, Model model){
        Order order = orderService.getOrderById(idOrder);
        model.addAttribute("order", order);
        return "admin/order/detailOrder";
    }
}
