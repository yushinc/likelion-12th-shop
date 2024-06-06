package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem {
    @Id
    @Column(name = "orderItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int price;
    private int count;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setPrice(item.getPrice());
        item.removeStock(count);

        return orderItem;
    }

    public int getTotalPrice() {
        return price * count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
