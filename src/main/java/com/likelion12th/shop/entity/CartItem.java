package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class CartItem {
    @Id
    @Column(name = "cartitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    //Item
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
}
