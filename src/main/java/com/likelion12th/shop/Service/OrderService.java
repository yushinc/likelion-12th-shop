package com.likelion12th.shop.Service;

import com.likelion12th.shop.Dto.CartOrderDto;
import com.likelion12th.shop.Dto.OrderDto;
import com.likelion12th.shop.Dto.OrderItemDto;
import com.likelion12th.shop.Dto.OrderReqDto;
import com.likelion12th.shop.entity.*;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    // 주문하기
    public Long createNewOrder(OrderReqDto orderReqDto, String email) {

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        // 회원이 존재하지 않을 경우 email로 새로운 회원 생성
        if (member == null) {
            Member member1 = new Member(); // member 객체 생성
            member1.setEmail(email);// 새로 생성한 member 객체에 이메일 설정
            member = memberRepository.save(member1); // 새로 생성한 member 객체를 데이터베이스에 저장

        }
        // orderReqDto 에서 상품 ID를 가져온다.
        Long itemId = orderReqDto.getItemId();
        // 상품 ID로 상품 정보를 데이터베이스에서 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품 ID 없음 : " + itemId));


        // 주문 상품 생성(상품과 주문 개수)
        // 주문할 상품과 수량으로 OrderItem 객체를 만드는 메소드 호출
        OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        // 회원과 아이템 리스트로 주문 생성하는 메소드를 호출
        Order order = Order.creatOrder(member, orderItems);
        // 생성된 주문을 저장
        orderRepository.save(order);

        // 생성된 주문의 ID 반환
        return order.getId();
    }

    // 모든 주문 내역 조회
    public List<OrderDto> getAllOrdersByUserEmail(String email) {
        // email로 주문을 조회하여 리스트에 저장
        List<Order> orders = orderRepository.findByMemberEmail(email);
        // 저장할 리스트를 생성
        List<OrderDto> OrderDtos = new ArrayList<>();

        //email로 조회한 각 주문에 대해 OrderDto로 변환하여 리스트에 추가
        orders.forEach(s -> OrderDtos.add(OrderDto.of(s))); // orders 리스트에 있는 각 주문을 반복하면서 OrderDto로 변환하여 이를 OrderDtos 리스트에 추가
        return OrderDtos;
    }

    // 주문 상세 조회
    public List<OrderItemDto> getOrderDetails(Long orderId, String email) {
        // orderId로 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        // 주문을 생성한 사용자인지 확인
        if (!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }

        // 주문에 속한 주문 상품들을 가져온다.
        List<OrderItem> orderItems = order.getOrderItems();
        // 비어있지 않을 경우
        if (!orderItems.isEmpty()) {
            // orderItems 리스트를 스트림으로 변환
            return orderItems.stream()

                    // orderItem 객체를 OrderItemDto 객체로 변환
                    .map(OrderItemDto::of)

                    // 변환된 OrderItemDto 객체들을 리스트로
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("주문 아이템이 없음");
        }
    }

    // 주문 취소
    public void cancelOrder(Long orderId, String email) {
        // 주문을 orderId로 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 :" + orderId));

        // 주문을 생성한 사용자인지 확인
        if (!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("취소 권한이 없음");
        }

        // 주문 상태를 "CANCEL"로 변경
        // 주문 취소한 수량만큼 상품의 재고를 증가
        order.cancelOrder();
        // 주문 정보 업데이트
        orderRepository.save(order);
    }

    public Long orders(List<OrderReqDto> orderDtoList, String email) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        // 주문할 상품 목록을 담을 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();

        // OrderReqDto 리스트를 순회하며 주문 아이템 생성
        for (OrderReqDto orderReqDto : orderDtoList) {

            // 주문할 상품의 ID로 상품 조회
            Item item = itemRepository.findById(orderReqDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            // "주문 아이템 생성" 하여 리스트에 추가 (힌트: 엔티티에 작성한 메서드 사용)
            OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());
            orderItems.add(orderItem);
        }
        // 회원과 주문 상품 목록으로 주문 객체 생성
        Order order = Order.creatOrder(member,orderItems);
        // 주문 저장
        orderRepository.save(order);

        // 생성된 주문의 ID 반환
        return order.getId();
    }
}