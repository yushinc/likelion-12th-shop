package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter

public class Item {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @Enumerated(EnumType.STRING)  //enum으로 설정한 코드
    private ItemSellStatus itemSellStatus;

}
