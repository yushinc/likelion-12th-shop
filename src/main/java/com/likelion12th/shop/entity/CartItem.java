package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class CartItem {
    @Id
    @Column (name = "cartItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
}
