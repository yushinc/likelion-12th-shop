package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.data.annotation.CreatedBy;

import java.security.PrivateKey;
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

    private LocalDateTime createdBy;
    private LocalDateTime orderDate;
    private LocalDateTime modifiedBy;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>(); // 초기화 추가

    public List<OrderItem> getOrderItemList() {
        return this.orderItemList;
    }

    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }

    //orderItemList에 주문 상품 정보를 담는다.

    public void addOrderItem(OrderItem orderItem) {
        orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }


    //회원과 아이템 아이템 리스트로 주문 생성하기

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        //주문 상태를 ORDER로 세팅한다.
        order.setOrderStatus(OrderStatus.ORDER);
        //현재 시간을 주문시간으로 세팅한다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItemList){
            //orderItem에서 메소드 호출
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 객체의 상태를 "취소"로 변경하고, 주문에 포함된 각 주문 아이템에 대해 취소 처리를 수행하는 기능을 구현
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItemList){
            orderItem.cancel();
        }
    }

}





