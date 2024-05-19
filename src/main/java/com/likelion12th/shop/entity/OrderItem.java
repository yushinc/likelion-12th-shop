package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name="order_item")
@Getter @Setter
public class OrderItem {
    @Id
    @Column(name="orderitem_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer orderPrice;
    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    //주문할 상품과 수량으로 OrderItem 객체를 만드는 메소드
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();

        //주문할 상품 세팅
        orderItem.setItem(item);

        //주문 수량 세팅
        orderItem.setCount(count);

        //상품 가격을 주문 가격으로 세팅
        orderItem.setOrderPrice(item.getPrice());

        //주문 수량만큼 상품의 재고 수량을 감소
        item.removeStock(count);
        return orderItem;
    }

    //주문 가격과 주문 수량을 곱해서 주문 총 가격을 계산
    public int getTotalPrice(){
        return orderPrice*count;
    }

    //주문을 취소할 경우 addStock 메소드 호출
    //주문 수량만큼 상품의 재고 증가
    public void cancel(){
        this.getItem().addStock(count);
    }
}


