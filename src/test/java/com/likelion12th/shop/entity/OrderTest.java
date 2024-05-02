package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.likelion12th.shop.entity.QOrderItem.orderItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;
    // 아이템 생성 메소드

    private Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.Sell); // Sell 대문자로 수정
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
            orderItem.setOrderQuantity(1);
            orderItem.setOrderPrice(1000);
            order.addOrderItem(orderItem); // 주문에 주문 상품 추가

        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new); //
        assertEquals(3, savedOrder.getOrderItems().size()); // 주문에 포함된 상품 수 확인
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItemList().remove(0);
        em.flush();
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            // item 생성
            Item item = this.createItem();
            itemRepository.save(item);

            // orderItem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderQuantity(1);
            orderItem.setOrderPrice(1000);
            order.addOrderItem(orderItem); // order의 주문 상품에 추가
        }

        // 회원 생성
        Member member = new Member();
        member.setMemberName("테스트 회원");
        order.setMember(member); // 주문과 회원을 연결

        // 회원 저장
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
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItemList().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: " + orderItem.getOrder().getClass());
        System.out.println("=================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("==================================");
    }

}