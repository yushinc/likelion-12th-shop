package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberEmail(String email);
}
