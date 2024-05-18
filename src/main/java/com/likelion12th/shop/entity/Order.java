package com.likelion12th.shop.entity;

import ch.qos.logback.core.BasicStatusManager;
import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.likelion12th.shop.entity.QOrder.order;

@Entity
@Table(name="orders") //mysql 예약어 order
@Setter
@Getter
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    // 주문 회원
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


    // 주문 항목을 추가하는 메서드
    public void addOrderItem(OrderItem orderItem) {
        // orderItem 객체를 order 객체의 orderItemList에 추가한다.
        orderItems.add(orderItem); // 주문 항목을 주문에 추가
        // Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계이므로
        // orderItem 객체에도 order 객체를 세팅한다.
        orderItem.setOrder(this); // 양방향 매핑을 위해 주문 항목의 주문을 설정
    }

    // 회원과 아이템 아이템 리스트로 주문 생성하기
    public static Order creatOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원의 정보를 세팅한다.

        // 장바구니 페이지에서는 한 번에 여러 개의 상품을 주문할 수 있으므로
        // 여러 개의 주문 상품을 담을 수 있도록 orderItem 객체를 추가한다.
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 총 금액을 구하는 메소드
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            // orderItem에서 메소드 호출
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            // orderItem에서 메소드 호출
            orderItem.cancel();
        }
    }
}