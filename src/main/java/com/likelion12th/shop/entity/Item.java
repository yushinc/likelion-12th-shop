package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.SellStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private SellStatus itemSellStatus;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

}
