package com.likelion12th.shop.entity;

import com.likelion12th.shop.Exception.OutOfStockException;
import com.likelion12th.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table (name = "item")    //name 지정 주의
@Getter @Setter
@ToString  //로그 생성안될 시, 추가하기!!
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;
    private String itemImg;
    private String itemImgPath;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    //상품의 재고를 감소시키는 로직
    public void removeStock(int stock) {
        int restStock = this.stock - stock;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stock + ")");
        }
        this.stock = restStock;
    }

    //주문 취소 시 상품의 재고를 증가시키는 로직
    public void addStock(int stock) {
        this.stock += stock;
    }
}



