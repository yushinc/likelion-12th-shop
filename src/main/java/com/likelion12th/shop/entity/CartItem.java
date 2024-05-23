package com.likelion12th.shop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
@Getter @Setter
public class CartItem {
    @Id
    @Column(name = "cartitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    private Integer count;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public static CartItem createCartItem(Cart cart,Item item, int count){
        CartItem cartItem=new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }
    public void addCount(int count){
        this.count+=count;
    }
    public void updateCount(int count){
        this.count=count;
    }


}
