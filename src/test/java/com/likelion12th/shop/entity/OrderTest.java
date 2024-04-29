package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.itemSellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class OrderTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(itemSellStatus.InStock);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0; i<3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setCreatedBy(LocalDateTime.now());
            orderItem.setModifiedBy(LocalDateTime.now());

            //주문 상품에 추가 - add 사용
            order.getOrderItems().add(orderItem);
        }


        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
        .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, order.getOrderItems().size());
    }

}