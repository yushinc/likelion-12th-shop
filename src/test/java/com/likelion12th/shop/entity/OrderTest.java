package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.SellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.Entity;
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

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class OrderTest {
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
    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상새 설명");
        item.setItemSellStatus(SellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }
    public Order createOrder(){
        Order order=new Order();
        for(int i=0;i<3;i++){
            //item 생성
            Item item = this.createItem();
            itemRepository.save(item);
            //orderitem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setCount(10);
            orderItem.setOderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItemlist().add(orderItem);
        }
        //회원 생성
        Member member = new Member();
        memberRepository.save(member);
        //주문 생성
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }


    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setCount(10);
            orderItem.setOderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItemlist().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        em.clear();
        Order saveOrder=orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3,order.getOrderItemlist().size());
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItemlist().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order=this.createOrder();
        //생성한 주문의 첫 번째 주문 상품 id 조회
        Long orderItemId=order.getOrderItemlist().get(0).getId();
        em.flush();
        em.clear();

        //id로 주문 상품 조회
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("================");
        orderItem.getOrder().getOrderDate();
        System.out.println("================");

    }
}