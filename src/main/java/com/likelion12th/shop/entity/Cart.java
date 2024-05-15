package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
}
