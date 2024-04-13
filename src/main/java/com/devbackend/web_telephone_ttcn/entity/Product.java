package com.devbackend.web_telephone_ttcn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "productName")
    private String productName;

    @Column(name = "quantityInStock")
    private Integer quantityInStock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "ram")
    private String ram;

    @Column(name = "battery")
    private String battery;

    @Column(name = "storage")
    private String storage;

    @Column(name = "color")
    private String color;

    @Column(name = "screenSize")
    private String screenSize;

    @Column(name = "chip")
    private String chip;

    @Column(name = "imagePath")
    private String imagePath;

    @Column(name = "isDelete")
    private int isDelete;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId",referencedColumnName = "id")
    private Category category;
}
