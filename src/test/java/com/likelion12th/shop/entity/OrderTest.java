package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // 아이템 생성 메소드
    private Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 내용");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
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
            orderItem.setCount(1);
            orderItem.setOrderPrice(1000);
            order.addOrderItem(orderItem); //주문에 주문상품 추가
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size()); //주문에 포함된 상품
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0); // 첫 번째 주문 항목을 제거합니다.
        em.flush();
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            //item 생성
            Item item = this.createItem();
            itemRepository.save(item);

            //orderItem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(1);
            orderItem.setOrderPrice(1000);
            order.addOrderItem(orderItem);
        }

        //회원 생성
        Member member = new Member();
        member.setName("테스트 회원");
        order.setMember(member); // 주문과 회원을 연결

        //회원 저장
        em.persist(member);

        // 주문 생성
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedBy(LocalDateTime.now());
        order.setModifiedBy(LocalDateTime.now());

        // 주문 저장
        orderRepository.save(order);

        return order;

    }

    @Test
    @DisplayName("지연로딩 테스트")
    public void lazLoadingTest() {
        Order order = this.createOrder();
        //생성한 주문의 첫번째 주문 상품 id 조회
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        //id로 주문 상품 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("================");
        orderItem.getOrder().getOrderDate();
        System.out.println("================");
    }
}


