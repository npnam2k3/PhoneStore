package com.devbackend.web_telephone_ttcn.controller.admin;

import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.service.OrderService;
import com.devbackend.web_telephone_ttcn.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class HomeController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private StatisticalService statisticalService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/home")
    public String home(Model model) {
        // thống kê doanh thu theo tháng
        Map<Integer, Integer> revenueMap = generateRevenueMap();
        if(!revenueMap.isEmpty() && revenueMap != null) {
            model.addAttribute("revenueMap", revenueMap);
        }

        // tinh tong doanh thu cua hang
        long totalRevenue = calculateTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue);

        // tinh so don hang bi huy
        int numberOrderCanceled = calculateOrderCanceled();
        model.addAttribute("numberOrderCanceled", numberOrderCanceled);

        // dem so luong user trong he thong
        int numberUser = countUser();
        model.addAttribute("numberUser", numberUser);

        // dem so luong hoa don
        int numberOrder = countOrder();
        model.addAttribute("numberOrder", numberOrder);

        // thong ke san pham ban chay
        List<Object[]> products = statisticalService.getTopSellingProducts();
        if(products.isEmpty()){
            model.addAttribute("listProductTopSellingEmpty", "Không có sản phẩm nào");
        }else {
            model.addAttribute("products", products);
        }

        // thong ke san pham ton kho
        List<Product> listProductInventory = statisticalService.getProductsInventory();
        if(!listProductInventory.isEmpty()){
            model.addAttribute("listProductInventory", listProductInventory);
        }else {
            model.addAttribute("listProductInventoryEmpty", "Không có sản phẩm nào");
        }

        // thống kê san pham sap het hang
        List<Product> listProductRunningOut = statisticalService.getProductsRunningOut();
        if(!listProductRunningOut.isEmpty()){
            model.addAttribute("listProductRunningOut", listProductRunningOut);
        }else {
            model.addAttribute("listProductRunningOutEmpty", "Không có sản phẩm nào");
        }
        return "/admin/home";
    }

    // hàm lấy Map tháng và doanh thu của tháng đó
    private Map<Integer, Integer> generateRevenueMap() {
        Map<Integer, Integer> revenueMap = new HashMap<>();

        // lấy năm hiện tại
        int currentYear = LocalDate.now().getYear();

        for (int month = 1; month <= 12; month++) {
            // lặp qua các tháng trong nam và lấy năm hiện tại truyền vào hàm
            List<Object[]> resultList = statisticalService.getRevenueByMonth(month, currentYear);
//            if(!resultList.isEmpty()) {
                int totalRevenue = calculateTotalRevenueByMonth(resultList);
                revenueMap.put(month, totalRevenue);
//            }
        }

        return revenueMap;
    }

    // tính tong doanh thu tra ve từ đối tượng trong database (hàm trong db trả về các bản ghi gồm 2 cột: month và revenue)
    // có dạng: 05-2024 - 73770000
    private int calculateTotalRevenueByMonth(List<Object[]> resultList) {
        int totalRevenue = 0;
        if(!resultList.isEmpty()) {
            for (Object[] result : resultList) {
                long revenue = (Long) result[1]; // Lấy giá trị doanh thu từ cột thứ hai
                totalRevenue += revenue;
            }
        }
        return totalRevenue;
    }

    // tính tổng doanh thu cửa hàng
    private long calculateTotalRevenue() {
        long totalRevenue = statisticalService.calculateTotalRevenue();
        return totalRevenue > 0 ? totalRevenue:0;
    }

    // tinh so don hang bi huy
    private int calculateOrderCanceled() {
        return statisticalService.calculateOrderCanceled() > 0 ? statisticalService.calculateOrderCanceled() : 0;
    }

    // dem so luong user trong he thong
    private int countUser() {
        return statisticalService.countUser() > 0 ? statisticalService.countUser() : 0;
    }

    // dem so luong don hang
    private int countOrder(){
        return statisticalService.countOrder()> 0 ? statisticalService.countOrder() : 0;
    }

    // thong ke 5 san pham ban chay nhat
//    private Object getTopSellingProducts(List<Object[]> resultList){
//        for (Object[] result: resultList){
//
//        }
//    }
}
