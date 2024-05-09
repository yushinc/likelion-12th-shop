package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@ToString
@Transactional
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime OrderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus OrderStatus;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    // 주문 회원
    @ManyToOne
    @JoinColumn(name = "member_id") // member_id라는 컬럼으로 저 member랑 조인을 하겠다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItemList = new ArrayList<>();

    //@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        this.orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }
}


