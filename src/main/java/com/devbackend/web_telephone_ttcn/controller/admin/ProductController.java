package com.devbackend.web_telephone_ttcn.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.dto.ProductDto;
import com.devbackend.web_telephone_ttcn.entity.Product;
import com.devbackend.web_telephone_ttcn.service.CategoryService;
import com.devbackend.web_telephone_ttcn.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class ProductController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    private Cloudinary cloudinary;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/product")
    public String managerProduct(Model model, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Product> products = productService.getAllPageable(pageNo);
        if(keyword!=null && !keyword.isEmpty()){
            products = productService.searchAndPageable(keyword, pageNo);
            if(products.isEmpty()){
                model.addAttribute("listNull", "Không tìm thấy sản phẩm với từ khóa tìm kiếm là: "+keyword);
            }
        }
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProduct", products);
        return "/admin/product/manageproduct";
    }

    @GetMapping("/product/add")
    public String addProduct(Model model) {
        List<CategoryDto> categoryDtos = categoryService.findAll();
        model.addAttribute("categories", categoryDtos);
        return "/admin/product/addproduct";
    }

    @PostMapping("/product/add")
    public String saveProduct(@RequestParam("productName") String productName,
                              @RequestParam("quantityInStock") String quantityInStockStr,
                              @RequestParam("color") String color,
                              @RequestParam("description") String description,
                              @RequestParam("price") String priceStr,
                              @RequestParam("screenSize") String screenSize,
                              @RequestParam("storage") String storage,
                              @RequestParam("ram") String ram,
                              @RequestParam("battery") String battery,
                              @RequestParam("chip") String chip,
                              @RequestParam("categoryId") String idStr, @RequestParam("pathFile") MultipartFile file, Model model) throws IOException {
        //validate
        boolean check = true;
        if (productName.trim() == null || productName.trim().isEmpty()) {
            model.addAttribute("productName", "Tên sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("name", productName);
        }
        Integer quantityInStock = null;
        try {
            quantityInStock = Integer.parseInt(quantityInStockStr);
            if (quantityInStock < 0) {
                model.addAttribute("quantityInStock", "Số lượng không được âm");
                check = false;
            } else {
                model.addAttribute("quantityValue", quantityInStock);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("quantityInStock", "Số lượng không hợp lệ");
            check = false;
        }
        if (color.trim() == null || color.trim().isEmpty()) {
            model.addAttribute("color", "Màu sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("colorValue", color);
        }
        if (description.trim() == null || description.trim().isEmpty()) {
            model.addAttribute("description", "Mô tả sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("descValue", description);
        }
        Integer price = null;
        try {
            price = Integer.parseInt(priceStr);
            if (price < 0) {
                model.addAttribute("price", "Giá không được âm");
                check = false;
            } else {
                model.addAttribute("priceValue", price);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("price", "Giá không hợp lệ");
            check = false;
        }
        if (screenSize.trim() == null || screenSize.trim().isEmpty()) {
            model.addAttribute("screenSize", "Kích thước màn hình của sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("screenValue", screenSize);
        }
        if (storage.trim() == null || storage.trim().isEmpty()) {
            model.addAttribute("storage", "Thông tin bộ nhớ của sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("storageValue", storage);
        }
        if (ram.trim() == null || ram.trim().isEmpty()) {
            model.addAttribute("ram", "Thông tin ram của sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("ramValue", ram);
        }
        if (battery.trim() == null || battery.trim().isEmpty()) {
            model.addAttribute("battery", "Thông tin pin của sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("batteryValue", battery);
        }
        if (chip.trim() == null || chip.trim().isEmpty()) {
            model.addAttribute("chip", "Thông tin chip của sản phẩm rỗng");
            check = false;
        } else {
            model.addAttribute("chipValue", chip);
        }
        Long id = null;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            model.addAttribute("categoryId", "Bạn chưa chọn danh mục");
            check = false;
        }
        if (file.isEmpty() || file == null) {
            model.addAttribute("file", "Bạn chưa chọn file");
            check = false;
        }

        if (!check) {
            List<CategoryDto> categoryDtos = categoryService.findAll();
            model.addAttribute("categories", categoryDtos);
            return "/admin/product/addproduct";
        } else {
            Map r = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String url = (String) r.get("secure_url");
            Product product = new Product();
            product.setProductName(productName);
            product.setQuantityInStock(quantityInStock);
            product.setColor(color);
            product.setDescription(description);
            product.setPrice(price);
            product.setScreenSize(screenSize);
            product.setStorage(storage);
            product.setRam(ram);
            product.setBattery(battery);
            product.setChip(chip);
            product.setCategory(categoryService.getById(id));
            product.setImagePath(url);
            product.setCreatedDate(new Date());
            product.setUpdatedDate(new Date());
            product.setIsDelete(0);
            productService.saveProduct(product);
            return "redirect:/admin/product";
        }
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product productOld = productService.findById(id);
        model.addAttribute("productOld", productOld);
        List<CategoryDto> categoryDtos = categoryService.findAll();
        model.addAttribute("categories", categoryDtos);
        return "/admin/product/updateproduct";
    }

    @PostMapping("/product/edit")
    public String updateProduct(@RequestParam("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam("quantityInStock") String quantityInStockStr,
            @RequestParam("color") String color,
            @RequestParam("description") String description,
            @RequestParam("price") String priceStr,
            @RequestParam("screenSize") String screenSize,
            @RequestParam("storage") String storage,
            @RequestParam("ram") String ram,
            @RequestParam("battery") String battery,
            @RequestParam("chip") String chip,
            @RequestParam("categoryId") String idCateStr, @RequestParam("pathFile") MultipartFile file,
            Model model) throws IOException {
        //validate
        boolean check = true;
        if (productName.trim() == null || productName.trim().isEmpty()) {
            model.addAttribute("productName", "Tên sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        }
        else {
            model.addAttribute("name", productName);
        }
        Integer quantityInStock = null;
        try {
            quantityInStock = Integer.parseInt(quantityInStockStr);
            if (quantityInStock < 0) {
                model.addAttribute("quantityInStock", "Số lượng không được âm (lấy thông tin cũ)");
                check = false;
            } else {
                model.addAttribute("quantityValue", quantityInStock);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("quantityInStock", "Số lượng không hợp lệ (lấy thông tin cũ)");
            check = false;
        }
        if (color.trim() == null || color.trim().isEmpty()) {
            model.addAttribute("color", "Màu sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("colorValue", color);
        }
        if (description.trim() == null || description.trim().isEmpty()) {
            model.addAttribute("description", "Mô tả sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("descValue", description);
        }
        Integer price = null;
        try {
            price = Integer.parseInt(priceStr);
            if (price < 0) {
                model.addAttribute("price", "Giá không được âm (lấy thông tin cũ)");
                check = false;
            } else {
                model.addAttribute("priceValue", price);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("price", "Giá không hợp lệ (lấy thông tin cũ)");
            check = false;
        }
        if (screenSize.trim() == null || screenSize.trim().isEmpty()) {
            model.addAttribute("screenSize", "Kích thước màn hình của sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("screenValue", screenSize);
        }
        if (storage.trim() == null || storage.trim().isEmpty()) {
            model.addAttribute("storage", "Thông tin bộ nhớ của sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("storageValue", storage);
        }
        if (ram.trim() == null || ram.trim().isEmpty()) {
            model.addAttribute("ram", "Thông tin ram của sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("ramValue", ram);
        }
        if (battery.trim() == null || battery.trim().isEmpty()) {
            model.addAttribute("battery", "Thông tin pin của sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("batteryValue", battery);
        }
        if (chip.trim() == null || chip.trim().isEmpty()) {
            model.addAttribute("chip", "Thông tin chip của sản phẩm rỗng (lấy thông tin cũ)");
            check = false;
        } else {
            model.addAttribute("chipValue", chip);
        }
        Long idCate = null;
        try {
            idCate = Long.parseLong(idCateStr);
        } catch (NumberFormatException e) {
            model.addAttribute("categoryId", "Bạn chưa chọn danh mục (lấy thông tin cũ)");
            check = false;
        }
        if (file.isEmpty() || file == null) {
            model.addAttribute("file", "Bạn chưa chọn file");
            check = false;
        }

        if (!check) {
//            Product product = (Product) session.getAttribute("productOld");
            List<CategoryDto> categoryDtos = categoryService.findAll();
            model.addAttribute("categories", categoryDtos);
//            Product productOld = productService.findById(product.getId());
            Product productOld = productService.findById(id);
            model.addAttribute("productOld", productOld);
            return "/admin/product/updateproduct";
//            return "redirect:/admin/product/edit/"+product.getId();
        } else {
            Map r = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String url = (String) r.get("secure_url");

            Product product = productService.findById(id);
            product.setProductName(productName);
            product.setQuantityInStock(quantityInStock);
            product.setColor(color);
            product.setDescription(description);
            product.setPrice(price);
            product.setScreenSize(screenSize);
            product.setStorage(storage);
            product.setRam(ram);
            product.setBattery(battery);
            product.setChip(chip);
            product.setCategory(categoryService.getById(idCate));
            product.setImagePath(url);
            product.setUpdatedDate(new Date());
            productService.saveProduct(product);
            return "redirect:/admin/product";
        }
    }

    @GetMapping("/product/detail/{id}")
    public String detailProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "/admin/product/productdetails";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }
}
