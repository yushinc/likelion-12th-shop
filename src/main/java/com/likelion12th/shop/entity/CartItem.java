package com.likelion12th.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter @Setter
public class CartItem {
    @Id
    @Column(name="cartitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //각각의 카트 아이템은 하나의 상품과 하나의 장바구니에만 속할 수 있음.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

        //장바구니에 있는 상품인 경우 수량 증가
        public void addCount(int count){
            this.count += count;
        }

        //장바구니 상품 수량 변경 시 수량 업데이트
        public void updateCount(int count){
            this.count = count;
        }
    }







