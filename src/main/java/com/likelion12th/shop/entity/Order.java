package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name="orders")
@Getter
public class Order {
    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //주문 회원
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;


    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
}
