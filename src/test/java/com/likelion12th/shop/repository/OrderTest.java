package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.likelion12th.shop.entity.QOrderItem.orderItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class OrderTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public OrderItem createOrderItem(Item item, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setCount(2);
        orderItem.setOrderPrice(item.getPrice() * orderItem.getCount());
        item.setStock(100);
        order.setRegTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return orderItem;
    }

    public Order createOrder() {
        Order order = new Order();
        order.setOrderItemList(new ArrayList<>());

        for (int i = 0; i < 3; i++) {
            // item 생성
            Item item = this.createItem();
            itemRepository.saveAndFlush(item);

            // orderitem 생성
            // ?QUA: 왜 orderitem은  order 저장하면 알아서 저장되는 방식인지 궁금 > OneToMany / ManyToOne 관계와 연관이 있는 것인지 궁금함
            OrderItem orderItem = this.createOrderItem(item, order);

            // orderitem 저장
            order.getOrderItemList().add(orderItem);
        }

        // member 생성
        Member member = new Member();
        member.setEmail("yearim1226@naver.com");
        member.setName("yerim");
        member.setPassword("1234");
        member.setAddress("seoul");

        // member 저장
        Member savedMember = memberRepository.saveAndFlush(member);
        System.out.println(savedMember.toString());

        // order 저장
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setRegTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        Order savedOrder = orderRepository.saveAndFlush(order);
        System.out.println(savedOrder.toString());

        return order;
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

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: " + orderItem.getOrder().getClass());

        System.out.println("===================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===================");
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(i);
            orderItem.setOrderPrice(1000 * i);
            orderItem.setOrder(order);


            order.getOrderItemList().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(
                        EntityNotFoundException::new
                );
        assertEquals(3, savedOrder.getOrderItemList().size());
    }


}