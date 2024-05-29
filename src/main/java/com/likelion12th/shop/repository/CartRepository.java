package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.CartItem;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);

}
