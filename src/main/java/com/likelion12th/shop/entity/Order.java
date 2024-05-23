package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders") // mysql 예약어 order가 있으므로
@Getter @Setter
public class Order extends BaseTime{
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")     // member_id: Order에 들어갈 변수명
    private Member member;

    // 고아객체 제거 기능 적용
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
   // @JoinColumn(name = "orderItem_id")
    List<OrderItem> orderItem = new ArrayList<>();

    private LocalDateTime orderDate;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    public void addOrderItem(OrderItem orderItemList){
        this.orderItem.add(orderItemList);
        orderItemList.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem1 : orderItem) {
            totalPrice += orderItem1.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCLE;
        for(OrderItem orderItem1 : orderItem) {
            orderItem1.cancel();
        }
    }
}