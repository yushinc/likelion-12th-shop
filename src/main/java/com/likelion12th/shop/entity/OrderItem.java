package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class OrderItem {
    @Id
    @Column(name = "orderitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    //Item
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer orderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
}
