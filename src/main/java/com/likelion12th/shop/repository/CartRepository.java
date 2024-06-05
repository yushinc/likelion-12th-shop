package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Member 엔티티를 기반으로 Cart 엔티티를 조회하는 findByMember 메서드 정의
    Cart findByMember(Member member);
}
