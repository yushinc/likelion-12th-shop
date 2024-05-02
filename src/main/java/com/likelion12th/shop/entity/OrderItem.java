package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="order_item")
@Getter @Setter
@ToString
public class OrderItem {
    @Id
    @Column(name = "orderitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer orderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
