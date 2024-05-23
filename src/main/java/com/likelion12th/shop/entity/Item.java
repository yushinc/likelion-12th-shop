package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemStatus;
import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int price;
    private int stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemSellStatus;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    private String itemImg;
    private String itemImgPath;

    // 아이템 생성 함수
    public static Item createItem(ItemFormDto itemFormDto) {
        Item item = new Item();
        item.setItemName(itemFormDto.getItemName());
        item.setPrice(item.getPrice());
        item.setStock(itemFormDto.getStock());
        item.setItemDetail(itemFormDto.getItemDetail());
        item.setItemSellStatus(ItemStatus.SELL);

        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }

    public void removeStock(int stock){
        int restStock = this.stock - stock;
        if (restStock<0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stock + ")");

        }
        this.stock = restStock;
    }

    public void addStock(int stock) { this.stock += stock;  }




}
