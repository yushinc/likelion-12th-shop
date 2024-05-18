package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.SellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime OrderDate;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus OrderStatus;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @ManyToOne
    @JoinColumn(name="member_id")//member의 pk랑 조인을 할 예정이고 name옵션은 외래키 컬럼명이다.
    private Member member;

    @OneToMany(mappedBy = "order" ,cascade=CascadeType.ALL, orphanRemoval = true)//연관관계 주인을 mapped by
    private List<OrderItem> orderItemlist=new ArrayList<>();
    public Item createItem(){
        Item item=new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상새 설명");
        item.setItemSellStatus(SellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }
    public void addOrderItem(OrderItem orderItem){
        orderItemlist.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(com.likelion12th.shop.constant.OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    public int getTotalPrice(){
        int totalPirce=0;
        for(OrderItem orderItem : orderItemlist){
            totalPirce+=orderItem.getTotalPrice();
        }
        return totalPirce;
    }
    public void cancelOrder(){
        this.OrderStatus=OrderStatus.CANCEL;
        for(OrderItem orderItem:orderItemlist){
            orderItem.cancel();
        }
    }

}
