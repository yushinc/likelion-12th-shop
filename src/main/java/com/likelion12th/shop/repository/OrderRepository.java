package com.likelion12th.shop.repository;
import com.likelion12th.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.likelion12th.shop.constant.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

