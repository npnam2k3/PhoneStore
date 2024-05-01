package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.CommentService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import com.devbackend.web_telephone_ttcn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @PostMapping("/comments")
    public String saveComment(@RequestParam("idProduct") String idStr,
                              @RequestParam("comment") String comment, Model model, RedirectAttributes redirectAttributes) {
        Long idProduct = Long.parseLong(idStr);
        Product product = productService.findById(idProduct);
        CategoryDto categoryDto = categoryService.findById(product.getCategory().getId());
        model.addAttribute("product", product);
        model.addAttribute("category", categoryDto);

        if (comment == null || comment.trim().isEmpty()) {
            System.out.println("Bình luận không hợp lệ");
            redirectAttributes.addAttribute("id", idProduct);
//            model.addAttribute("commentError", "Bình luận không hợp lệ!");
            redirectAttributes.addFlashAttribute("commentError", "Bình luận không hợp lệ!");
//            return "user/productDetail";
            return "redirect:/productDetail";
        } else {
            if (isLoggedIn()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userService.findUserByUsername(username);
                Long userId = user.getId();

                System.out.println("Lưu vào trong database");
                if(commentService.addComment(idProduct, userId, comment)){
                    //dùng để thiết lập đưởng dẫn có dạng ?id= => rồi dùng @RequestParam("id") để lấy id
                    redirectAttributes.addAttribute("id", idProduct);
                    redirectAttributes.addFlashAttribute("success", "Gửi bình luận thành công!");
                    return "redirect:/productDetail";
                }else{
                    model.addAttribute("failed", "Không thể gửi bình luận!");
                    return "user/productDetail";
                }
            } else {
                System.out.println("You have to login to comment");
//                model.addAttribute("notLogin", "Bạn cần đăng nhập để bình luận!");
//                return "user/productDetail";
                redirectAttributes.addAttribute("id", idProduct);
                redirectAttributes.addFlashAttribute("notLogin", "Bạn cần đăng nhập để bình luận!");
                return "redirect:/productDetail";
            }
        }
    }

    private boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

}
