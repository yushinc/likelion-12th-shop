package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
public class Item {
    public static Item item;

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

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


}
