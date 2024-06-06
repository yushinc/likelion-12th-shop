package com.likelion12th.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter @Setter
public class Cart {
    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdBY;
    private LocalDateTime modifiedBY;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
