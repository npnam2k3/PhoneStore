package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class CategoryClientController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }
    @GetMapping("allCategory")
    public String allCategory(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        List<CategoryDto> categoryDtoList = categoryService.getAll();

        model.addAttribute("listCategory", categoryDtoList);

//        List<Product> productList = productService.findAll();
        Page<Product> products = productService.getAllPageable(pageNo);
        model.addAttribute("listProduct", products);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        return "user/allCategory";
    }
    @GetMapping("category/{id}")
    public String getProductByCategory(@PathVariable("id") Long idCategory, Model model){
        CategoryDto categoryDto = categoryService.findById(idCategory);
        model.addAttribute("category", categoryDto);
        List<Product> list = productService.getListProductByCategoryId(idCategory);
        if(list.isEmpty()){
            model.addAttribute("listProductEmpty", "Không có sản phẩm");
        }else {
            model.addAttribute("listProduct", list);
        }
        return "user/listProductByCategory";
    }
}
