package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);
}
