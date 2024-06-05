package com.likelion12th.shop.repository;

import ch.qos.logback.core.BasicStatusManager;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

         EntityManager em;
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i=0; i<3; i++) {
            Item item = this.createItem();
            // 생성한 아이템 세팅
            // 주문 수량 -
            // 주문 가격 - 10000
            // 주문 세팅

            // 주문 상품에 추가
        }

    }

    private Item createItem() {

        return null;
    }
}