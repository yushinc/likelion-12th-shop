package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.itemSellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.swing.plaf.synth.SynthUI;
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
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


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

    public Order createOrder() {
        Order order = new Order();

        for(int i=0; i<3; i++) {
            // item 생성
            Item item = createItem();
            itemRepository.save(item);

            // orderItem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderPrice(10000);
            orderItem.setCount(1);
            orderItem.setOrder(order);
            orderItem.setCreatedBy(LocalDateTime.now());
            orderItem.setModifiedBy(LocalDateTime.now());

            // order에 주문 상품 추가
            order.getOrderItemList().add(orderItem);
        }

        // 회원 생성
        Member member = new Member();
        memberRepository.save(member);

        // 주문 생성
        order.setMember(member);
        orderRepository.save(order);

        return order;
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

            //주문 상품에 추가
            order.getOrderItemList().add(orderItem);
        }


        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
        .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, order.getOrderItemList().size());
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItemList().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItemList().get(0).getId();
        em.flush();
        em.clear();

        //id로 주문 상품 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: " + orderItem.getOrder().getClass());
        System.out.println("====================================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("====================================================");
    }
}