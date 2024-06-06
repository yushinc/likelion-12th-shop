package com.likelion12th.shop.entity;



import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static com.likelion12th.shop.entity.QOrderItem.orderItem;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application.properties")
class OrderTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired //의존성 주입
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Item createItem() {
        //Item 객체를 생성
        Item item = new Item();

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
    public void cascadeTest() {
        Order order = new Order();

        for (int i=0; i<3; i++) {
            //3개의 Item을 생성하여 Order에 추가

            Item item = this.createItem();
            //createItem() 메소드를 사용하여 새로운 Item을 생성

            itemRepository.save(item);
            //생성된 Item을 저장소에 저장

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItemList().add(orderItem);
            //Order 객체에 OrderItem을 추가
        }

        orderRepository.saveAndFlush(order);
        //Order 객체를 저장소에 저장하고 영속성을 확실히 하기 위해 flush()를 호출

        em.clear();
        //영속성 컨텍스트를 초기화

        System.out.println("order.id =" + order.getId());
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        //저장된 Order를 조회하여 가져옴

        assertEquals(3, savedOrder.getOrderItemList().size());
        //저장된 Order의 OrderItem의 개수가 예상과 일치하는 지 확인
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItemList().remove(0);
        //Order에서 첫번째 OrderItem을 제거
        
        em.flush();
        //영속성 컨텍스트의 변경 사항을 데이터베이스에 반영
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            //3개의 Item을 생성하여 Order에 추가
            
            Item item = createItem();
            //createItem() 메소드를 사용하여 새로운 Item을 생성
            
            itemRepository.save(item);
            //생성된 Item을 저장소에 저장
            
            OrderItem orderItem = new OrderItem();
            //새로운 OrderItem 객체를 생성
            
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            //order의 주문 상품에 추가
            order.getOrderItemList().add(orderItem);
        }
        
        //여기부터
        Member member = new Member();
        memberRepository.save(member);
        //여기까지 새로운 회원(Member)을 생성하고 저장
        
        order.setMember(member);
        //Order에 회원을 설정
        
        orderRepository.save(order);
        //Order 객체를 저장소에 저장

        return order;
    }


    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItemList().get(0).getId();
        //생성된 Order에서 첫번째 OrderItem의 ID를 가져옴

        em.flush();
        //영속성 컨텍스트의 변경 사항을 데이터베이스에 반영

        em.clear();
        //영속성 컨텍스트를 초기화


        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        //OrderItemId를 사용하여 해당 OrderItem을 조회

        System.out.println("Order class: " + orderItem.getOrder().getClass());
        //OrderItem의 소속된 Order의 클래스를 출력하여 Lazy Loading을 확인

        //Lazy Loading이 발생하는 지 확인하기 위해 OrderItem의 Order에 접근
        System.out.println("=================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("==================================");

    }
}