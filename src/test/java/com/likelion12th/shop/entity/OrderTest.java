package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class OrderTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderItemRepository orderItemRepository;


    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();

        for (int i = 0; i < 3; i ++ ){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(i);
            orderItem.setPrice(1000);

            order.getOrderItem().add(orderItem);


        }
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(3, order.getOrderItem().size());
    }

    public Order createOrder() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            //item 생성
            Item item = createItem();
            itemRepository.save(item);

            //orderItem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);

            //order의 주문 상품에 추가
            order.getOrderItem().add(orderItem);

        }

        // 회원 생성
        Member member = new Member();
        memberRepository.save(member);

        //주문 생성
        order.setMember(member);
        orderRepository.save(order);

        System.out.println("orderItem: "+ order.getOrderItem());
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트 ")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        System.out.println("orderItem 2: "+ order.getOrderItem());
        order.getOrderItem().remove(0);
        System.out.println("orderItem 3: "+ order.getOrderItem());
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        // 주문 생성
        Order order = this.createOrder();

        // id로 상품 조회
        Long orderItemId = order.getOrderItem().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class: " + orderItem.getOrder().getClass());

        System.out.println("----------------------");
        System.out.println(orderItem.getOrder().getOrderDate());
        System.out.println("----------------------");
    }


}