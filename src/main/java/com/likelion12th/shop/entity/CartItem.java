package com.likelion12th.shop.entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
@Getter
public class CartItem {
    @Id
    @Column(name = "cartitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


}
