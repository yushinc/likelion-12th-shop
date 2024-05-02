package com.likelion12th.shop.entity;

import ch.qos.logback.core.BasicStatusManager;
import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.likelion12th.shop.entity.QOrder.order;

@Entity
@Table(name="orders") //mysql 예약어 order
@Getter
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
              orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    // 주문 회원
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

    private LocalDateTime OrderDate;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


    // 주문 항목을 추가하는 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); // 주문 항목을 주문에 추가
        orderItem.setOrder(this); // 양방향 매핑을 위해 주문 항목의 주문을 설정
    }

    public List<OrderItem> getOrderItemList() {
        return this.orderItems;
    }

    public void setMember(Member member) {
    }

    public void setOrderStatus(OrderStatus orderStatus) {
    }

    public void setOrderDate(LocalDateTime now) {
    }

    public void setCreatedBy(LocalDateTime now) {
    }

    public void setModifiedBy(LocalDateTime now) {
    }

    public EntityManager getOrderItemlist() {
        return null;
    }
}