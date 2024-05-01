package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.Cart;
import com.devbackend.web_telephone_ttcn.entity.CartItem;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.CartService;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import com.devbackend.web_telephone_ttcn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addLoggedInUser(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        // lay so luong trong gio hang ghi vao session
        User user = userService.findUserByUsername(username);
        if(user!=null){
            Cart cart = cartService.findByUserId(user.getId());
            if (cart != null) {
                List<CartItem> list = cartService.getListCartItemByCartId(cart.getId());
                if(!list.isEmpty()){
                    int quantityCartItemInCart = list.size();
                    session.setAttribute("quantityCartItem", quantityCartItemInCart);
                }
            }
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        List<CategoryDto> categoryDtoList = categoryService.findAll();
        for (CategoryDto categoryDto : categoryDtoList) {
            List<Product> products = productService.getListProductByCategoryId(categoryDto.getId());
            categoryDto.setProducts(products);
        }
        model.addAttribute("listCategory", categoryDtoList);
        return "user/index";
    }

    @GetMapping("/accountOfCustomer")
    public String accountOfCustomer(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("userLogin", user);
        return "user/accountOfCustomer";
    }

    @PostMapping("/accountOfCustomer")
    public String updateInforCustomer(@RequestParam("idUser") Long idUser,
                                      @RequestParam("fullName") String fullname,
                                      @RequestParam("address") String address) {
        UserDto userDto = userService.findById(idUser);
        if (fullname != null && !fullname.trim().isEmpty()) {
            userDto.setFullname(fullname);
        }
        if (address != null && !address.trim().isEmpty()) {
            userDto.setAddress(address);
        }
        userService.updateCustomer(userDto);
//        System.out.println(userDto);
        return "redirect:/accountOfCustomer";
    }

    @GetMapping("/changePasswordForm")
    public String changePasswordForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        Long id = user.getId();
        model.addAttribute("userId", id);
        return "user/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("password") String passwordOld,
                                 @RequestParam("new_password") String newPassword,
                                 @RequestParam("confirm_new_password") String confirmNewPassword,
                                 @RequestParam(name = "idUser") String idStr,
                                 Model model) {
        List<String> errors = new ArrayList<>();
        Long id = null;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            errors.add("Id invalid");
            System.out.println("Id không hợp lệ");
        }
        if (passwordOld == null || passwordOld.trim().isEmpty()) {
            errors.add("Password empty");
            model.addAttribute("passwordError", "Mật khẩu rỗng");
        } else {
            System.out.println(id);
            if (!userService.checkPasswordExists(passwordOld, id)) {
                errors.add("Password invalid");
                model.addAttribute("passwordError", "Mật khẩu không chính xác");
            }

        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.add("New password empty");
            model.addAttribute("newPasswordError", "Mật khẩu mới rỗng");
        }
        if (confirmNewPassword == null || confirmNewPassword.trim().isEmpty()) {
            errors.add("New password confirm empty");
            model.addAttribute("passwordConfirmError", "Mật khẩu xác nhận rỗng");
        } else {
            if (!confirmNewPassword.equals(newPassword)) {
                errors.add("Password confirm invalid");
                model.addAttribute("passwordConfirmError", "Mật khẩu xác nhận không khớp");
            }
        }
        if (!errors.isEmpty()) {
            // nếu có lỗi
            model.addAttribute("userId", id);
            return "user/changePassword";
        }
        System.out.println("Không lỗi");
        userService.changePassword(id, newPassword);
        return "redirect:/logout";
    }
}
