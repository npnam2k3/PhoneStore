package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.Comments;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.CommentService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class ProductClientController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("productDetail")
    public String productDetail(@RequestParam("id") Long id, Model model) {
        Product product = productService.findById(id);
        CategoryDto categoryDto = categoryService.findById(product.getCategory().getId());
        List<Comments> comments = commentService.getCommentsConfirmed(id);

        model.addAttribute("product", product);
        model.addAttribute("category", categoryDto);
        model.addAttribute("listComment", comments);
        return "user/productDetail";
    }

    @GetMapping("/product/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        System.out.println("Từ khóa tìm kiếm: " + keyword);

        Page<Product> products = productService.searchAndPageable(keyword, pageNo);
        if (products.isEmpty()) {
            model.addAttribute("listNull", "Không tìm thấy sản phẩm với từ khóa tìm kiếm là: " + keyword);
        }

        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProduct", products);
        return "user/search";
    }
}
