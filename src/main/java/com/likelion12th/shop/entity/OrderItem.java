package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer orderPrice;

    private Integer count;

    private LocalDateTime createdBy;

    private LocalDateTime modifiedBy;

}