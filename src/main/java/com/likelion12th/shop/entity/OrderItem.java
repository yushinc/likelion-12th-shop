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
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer oderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }
    public int getTotalPrice(){
        return count*oderPrice;
    }
    public void cancel(){
        this.getItem().addStock(count); //count만큼 주문하고 다시 취소
    }
}
