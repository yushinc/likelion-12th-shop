package com.likelion12th.shop.entity;

import com.likelion12th.shop.Exception.OutOfStockException;
import com.likelion12th.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter
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
    private String itemImg; // 추가한 필드
    private String itemImgPath; // 추가한 필드

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    // 상품의 재고를 감소시키는 로직
    public void removeStock(int stock) {
        int restStock = this.stock - stock;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량:" + this.stock + ")");
        }
        this.stock = restStock;
    }

    // 주문 취소 시 상품의 재고를 증가시키는 로직
    public void addStock(int stock) {
        this.stock += stock;
    }
}