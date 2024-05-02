package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Order;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
