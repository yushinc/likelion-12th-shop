package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.MessageUtil;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {


    public static Object out;

    public static void main(String[] args) {
        System.out.println("Message");
    }

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

    private Integer orderPrice;
    private Integer count;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public void setPrice(int i) {
    }

    public void setQuantity(int i) {
    }

    public void setOrderQuantity(int i) {
    }
}
