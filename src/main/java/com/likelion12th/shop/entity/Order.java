package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id
    @Column(name = "oder_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime OrderDate;
    private OrderStatus OrderStatus;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @ManyToOne
    @JoinColumn(name="member_id")//member의 pk랑 조인을 할 예정이고 name옵션은 외래키 컬럼명이다.
    private Member member;
}
