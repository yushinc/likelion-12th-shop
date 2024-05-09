package com.likelion12th.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter @Setter
public class CartItem {
    @Id
    @Column(name="cartitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
    private Integer count;

    //각각의 카트 아이템은 하나의 상품과 하나의 장바구니에만 속할 수 있음.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}





