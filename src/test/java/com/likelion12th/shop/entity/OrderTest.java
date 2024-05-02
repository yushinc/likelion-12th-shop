package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
@TestPropertySource(locations="classpath:application.properties")
class OrderTest {

    @Autowired
    private ItemRepository itemrepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    public Item createItem(){
        Item item=new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item=this.createItem();
            itemrepository.save(item);

            OrderItem orderItem=new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(2);
            orderItem.setOrderPrice(1000);
            orderItem.setCreatedBy(LocalDateTime.now());

            //주문 상품에 추가 -add 사용
            order.getOrderItems().add(orderItem);


        }
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder=orderRepository.findById(order.getId())
        .orElseThrow(EntityNotFoundException::new);
        assertEquals(3,order.getOrderItems().size());

    }

    public Order createOrder(){
        Order order=new Order();

        for(int i=0; i<3; i++){
            Item item=createItem();
            itemrepository.save(item);

            OrderItem orderItem=new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderPrice(10000);
            orderItem.setCount(1);
            orderItem.setOrder(order);
            orderItem.setCreatedBy(LocalDateTime.now());
            orderItem.setModifiedBy(LocalDateTime.now());


            //order의 주문 상품에 추가
            //order.getOrderItemList().add(orderItem);
        }
        //회원 생성
        Member member=new Member();
        memberRepository.save(member);
        //주문 생성
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order=this.createOrder();
        order.getOrderItemList().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){

    }




}