package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.likelion12th.shop.entity.QOrder.order;

@Entity
@Table(name = "orders") //mysql 예약어 order
@Getter @Setter
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //양방향 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>(); // 초기화 추가

    //주문 회원
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdBy;
    private LocalDateTime orderDate;
    private LocalDateTime modifiedBy;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;


    //주문 항목을 추가하는 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        //Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계이므로 orderItem 객체에도 order 객체를 세팅한다.
        orderItem.setOrder(this); // 양방향 매핑을 위해 주문 항목의 주문을 설정
    }

    //회원과 아이템 아이템 리스트로 주문 생성하기. 여기서 orderItems로 바꿨었음.
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);

        // 장바구니 페이지에서는 한 번에 여러 개의 상품을 주문할 수 있으므로
        // 여러 개의 주문 상품을 담을 수 있도록 orderItem 객체를 추가한다.
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        //주문 상태를 ORDER로 세팅한다.
        order.setOrderStatus(OrderStatus.ORDER);
        //현재 시간을 주문시간으로 세팅한다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 총 금액을 구하는 메소드
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            //orderItem에서 메소드 호출
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 객체의 상태를 "취소"로 변경하고, 주문에 포함된 각 주문 아이템에 대해 취소 처리를 수행하는 기능을 구현
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

}





