package com.likelion12th.shop.entity;

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

    }
