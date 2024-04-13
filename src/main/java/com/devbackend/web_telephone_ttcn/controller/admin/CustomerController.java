package com.devbackend.web_telephone_ttcn.controller.admin;

import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class CustomerController {
    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }
    @GetMapping("/home")
    public String home(){
        return "/admin/home";
    }
    @GetMapping("/user")
    public String listUser(Model model, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        Page<User> userDto = userService.getAll(pageNo);
        if(keyword!=null && !keyword.isEmpty()){
            userDto = userService.searchAndPage(keyword, pageNo);
            if(userDto.isEmpty()){
                model.addAttribute("listNull", "Không tìm thấy tài khoản với từ khóa tìm kiếm là: "+keyword);
            }
        }
//        System.out.println(userDto);
        model.addAttribute("totalPage", userDto.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listUser", userDto);
        return "/admin/user/customermanagement";
    }

    @GetMapping("/user/detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model){
        UserDto user = userService.findById(id);
        model.addAttribute("user", user);
        return "/admin/user/customerdetail";
    }

    @GetMapping("/user/deleteOn/{id}")
    public String deleteOn(@PathVariable("id") Long id, Model model){
        if(userService.deleteOn(id)){
//            model.addAttribute("message", "Đã kích hoạt thành công!");
            return "redirect:/admin/user";
        }else {
//            model.addAttribute("message", "Kích hoạt thất bại ");
            return "redirect:/admin/user";
        }
    }

    @GetMapping("/user/deleteOff/{id}")
    public String deleteOff(@PathVariable("id") Long id, Model model){
        if(userService.deleteOff(id)){
//            model.addAttribute("message", "Đã vô hiệu hóa thành công!");
            return "redirect:/admin/user";
        }else {
//            model.addAttribute("message", "Vô hiệu hóa thất bại ");
            return "redirect:/admin/user";
        }
    }
}
