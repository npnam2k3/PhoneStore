package com.devbackend.web_telephone_ttcn.dto;

import com.devbackend.web_telephone_ttcn.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
    private Long id;
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String productName;
    @NotEmpty(message = "Số không được để trống")
    private Integer quantityInStock;
    @NotEmpty(message = "Giá không được để trống")
    private Integer price;
    @NotEmpty(message = "Ram không được để trống")
    private String ram;
    @NotEmpty(message = "Pin không được để trống")
    private String battery;
    @NotEmpty(message = "Bộ nhớ không được để trống")
    private String storage;
    @NotEmpty(message = "Màu sản phẩm không được để trống")
    private String color;
    @NotEmpty(message = "Kích thước không được để trống")
    private String screenSize;
    @NotEmpty(message = "Chip không được để trống")
    private String chip;
    @NotEmpty(message = "Bạn chưa chọn file ảnh")
    private MultipartFile pathFile;
    private String imagePath;
    @NotEmpty(message = "Mô tả không được để trống")
    private String description;
    private int isDelete;
    @NotEmpty(message = "Bạn chưa chọn danh mục")
    private Category category;
}
