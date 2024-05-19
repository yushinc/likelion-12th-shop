package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table
@Getter
public class Cart {
    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="member_id") //이름만 저거라고 생각하기
    private Member member;


    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

}
