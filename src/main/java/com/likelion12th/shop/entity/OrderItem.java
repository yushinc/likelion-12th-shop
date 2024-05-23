package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item") // 테이블 이름 지정
@Getter
@Setter
@ToString
@Transactional
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;
    private int count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public static OrderItem createOrderItem (Item item, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
