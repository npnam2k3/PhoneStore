package com.devbackend.web_telephone_ttcn.controller.admin;

import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/category")
    public String category(Model model, @Param("keyword") String keyword) {
        List<CategoryDto> categoryDto = categoryService.getAll();
        if(keyword!=null && !keyword.isEmpty()){
            categoryDto = categoryService.searchCategory(keyword);
        }
        model.addAttribute("listCategory", categoryDto);
        return "/admin/category/categorymanagement";
    }

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/category/add")
    public String addCategory(Model model) {
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        return "/admin/category/addcategory";
    }

    @PostMapping("/category/add")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            return "/admin/category/addcategory"; //co data kem theo
        }
        categoryService.saveCategory(categoryDto);
        return "redirect:/admin/category"; //không có data kem theo
    }

    @GetMapping("/category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        CategoryDto categoryDto = categoryService.findById(id);
        model.addAttribute("category", categoryDto);
        return "/admin/category/updatecategory";
    }

    @PostMapping("/category/edit")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result, Model model){
//        System.out.println(categoryDto);
        if(result.hasErrors()){
            model.addAttribute("category", categoryDto);
            return "/admin/category/updatecategory";
        }
        categoryService.saveCategory(categoryDto);
        return "redirect:/admin/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        if(categoryService.deleteCategory(id)){
            return "redirect:/admin/category";
        }
        return "redirect:/admin/category";
    }

    // trang danh sach san pham theo danh muc
    @GetMapping("/category/phonelist/{id}")
    public String listProductByCategory(@PathVariable("id") Long id, Model model, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        CategoryDto categoryDto = categoryService.findById(id);
        model.addAttribute("category", categoryDto);

        Page<Product> list = productService.getListProductByCategoryIdAndPageable(categoryDto.getId(),pageNo);
        if(keyword!=null && !keyword.isEmpty()){
            list = productService.getListProductByCategoryIdAndPageableAndSearch(keyword, categoryDto.getId(), pageNo);
            if(list.isEmpty()){
                model.addAttribute("listNull", "Không tìm thấy sản phẩm với từ khóa: "+keyword);
            }
        }
        if(list.isEmpty()){
            model.addAttribute("listEmpty", " Không có sản phẩm nào ");
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProduct", list);
        return "/admin/category/phonelist";
    }

}
