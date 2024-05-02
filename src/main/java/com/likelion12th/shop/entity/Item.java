package com.likelion12th.shop.entity;

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


    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemsellstatus;

    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems;


    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


}
