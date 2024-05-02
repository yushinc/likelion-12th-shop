package com.likelion12th.shop.entity;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.Dto.ProductDTO;
import com.likelion12th.shop.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
@Entity
@Table(name = "product")
@Getter @Setter @ToString
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String Category;
    private String size;
    private String color;
    private Integer price;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName()); // 이름
        product.setPrice((int) productDTO.getPrice()); // 가격
        product.setSize(productDTO.getSize()); // 사이즈
        product.setColor(productDTO.getColor()); // 색상
        product.setCategory(productDTO.getCategory()); // 카테고리
        // Set other attributes if needed

        return product;
    }
}