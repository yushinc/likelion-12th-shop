package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
   // List<OrderItem> findByOrderItemId(Long orderItemId);         // 상품명으로 상품 조회하는 쿼리메소드 생성
}
