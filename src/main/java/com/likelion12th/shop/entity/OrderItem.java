package com.likelion12th.shop.entity;

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

    public static Object out;

    public static void main(String[] args) {
        System.out.println("Message");
    }

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer orderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    //주문가격과 주문수량을 곱해서 주문 총 가격을 계산
    public int getTotalPrice(){
        return orderPrice*count;
    }
    //주문을 취소할 경우 addstock 메서도 호출
    //주문 수량만큼 상품의 재고 증가

    public void cancel(){
        this.getItem().addStock(count);
    }

}
