package com.likelion12th.shop.entity;

import com.likelion12th.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;
    private String itemImg;
    private String itemImgPath;

    @Enumerated(EnumType.STRING)
    private com.likelion12th.shop.constant.ItemSellStatus ItemSellStatus;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    public void removeStock(int stock) {
        int restStock = this.stock - stock;
        if(restStock<0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stock + ")");
        }
        this.stock = restStock;
    }

    public void addStock(int stock) {
        this.stock += stock;
    }
}


