package com.likelion12th.shop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter @Setter @ToString
public class OrderItem {
    @Id
    @Column(name = "orderitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private Integer oderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


}
