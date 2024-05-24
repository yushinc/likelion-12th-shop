package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer count;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    // 장바구니에 담을 상품 엔티티 생성
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 있는 상품인 경우 수량 증가
    public void addCount(int count){
        this.count += count;
    }
    // 장바구니 상품 수량 변경 시 수량 업데이트
    public void updateCount(int count){
        this.count = count;
    }
}
