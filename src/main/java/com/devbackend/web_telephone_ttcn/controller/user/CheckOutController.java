package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.entity.CartItem;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.UserService;
import com.devbackend.web_telephone_ttcn.util.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {
    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @PostMapping("/buyNow")
    public String buyNow(@RequestParam("itemChecked")List<CartItem> list, Model model, HttpSession session){
        // cách 1: khi lấy từ bên client về sẽ ánh xạ thanh danh sach cac cart item luon
        // cach 2: khi lấy từ bên client về sẽ trả về danh sach id của các cartItem đã chọn
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);

        int totalCheckout = 0;
        for (CartItem item: list){
            totalCheckout+=item.getTotalPrice();
        }
        session.setAttribute("listItem", list);
        session.setAttribute("totalCheckout",totalCheckout);
        model.addAttribute("listItem", list);
        model.addAttribute("totalCheckout", totalCheckout);
        model.addAttribute("user", user);
        return "user/checkoutInfo";
    }
//@PostMapping("/buyNow")
//public String buyNow(@RequestParam("itemChecked")List<String> list){
//    for(String item: list){
//        System.out.println(item);
//    }
//    return "redirect:/";
//}

    @PostMapping("/receiveAtStore")
    public String receiveAtStore(@RequestParam("fullname") String fullname,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("email") String email,
                                 @RequestParam("address") String address,
                                 @RequestParam("total-price") String totalPriceStr,
                                 Model model){
        int totalPrice = -1;
        try {
            totalPrice = Integer.parseInt(totalPriceStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<String> inforList = new ArrayList<>();
        inforList.add(fullname);
        inforList.add(phoneNumber);
        inforList.add(email);
        inforList.add(address);
        model.addAttribute("inforList",inforList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("receive","Thông tin nhận hàng");
        model.addAttribute("address", "Địa chỉ cửa hàng");
        model.addAttribute("receiveMode", Constant.RECEIVE_MODE_AT_STORE);
        return "user/confirmOrder";
    }

    @PostMapping("/deliveryAtHome")
    public String deliveryAtHome(@RequestParam("fullname") String fullname,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("email") String email,
                                 @RequestParam("address") String address,
                                 @RequestParam("total-price") String totalPriceStr, Model model){
        int totalPrice = -1;
        try {
            totalPrice = Integer.parseInt(totalPriceStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<String> inforList = new ArrayList<>();
        inforList.add(fullname);
        inforList.add(phoneNumber);
        inforList.add(email);
        inforList.add(address);
        model.addAttribute("inforList",inforList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("delivery","Thông tin giao hàng");
        model.addAttribute("address", "Địa chỉ nhận hàng");
        model.addAttribute("receiveMode", Constant.RECEIVE_MODE_AT_HOME);
        return "user/confirmOrder";
    }
}
