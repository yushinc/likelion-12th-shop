package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table
@Getter
@Setter
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

    @CreatedBy
    private LocalDateTime createdBy;
    @LastModifiedBy
    private LocalDateTime modifiedBy;

    public static Cart createCart(Member member){
        Cart cart=new Cart();
        cart.setMember(member);
        return cart;
    }

}
