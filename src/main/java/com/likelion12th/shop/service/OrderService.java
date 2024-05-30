package com.likelion12th.shop.service;


import com.likelion12th.shop.dto.*;
import com.likelion12th.shop.entity.*;
import com.likelion12th.shop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    public Long createNewOrder(OrderReqDto orderReqDto, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail);

        if (member == null) {
            Member newMember = new Member();
            newMember.setEmail(memberEmail);
            member = memberRepository.save(newMember);
        }

        Long itemId = orderReqDto.getItemId();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품 ID 없음 : " + itemId));


        // QUA?? order id 지정해주지 않아도 괜찮은 건가?
        OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.createOrder(member, orderItems);

        orderRepository.save(order);

        return order.getId();
    }

    public List<OrderDto> getAllOrdersByUserEmail(String email) {
        List<Order> orders = orderRepository.findByMemberEmail(email);

        List<OrderDto> orderDtos = new ArrayList<>();

        orders.forEach(s -> orderDtos.add(OrderDto.of(s)));
        return orderDtos;
    }

    public List<OrderItemDto> getOrderDetails(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if (!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }

        List<OrderItem> orderItems = order.getOrderItemList();

        if (!orderItems.isEmpty()) {
            // orderItems 리스트를 스트림으로 변환
            return orderItems.stream()
                    .map(OrderItemDto::of)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("주문 아이템이 없음");
        }
    }

    public void cancelOrder(Long orderId, String email){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if(!order.getMember().getEmail().equals(email)){
            throw new IllegalArgumentException("취소 권한이 없음");
        }

        order.cancelOrder();

        orderRepository.save(order);
    }

    public Long orders(List<OrderReqDto> orderDtoList, String email){
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderReqDto orderReqDto: orderDtoList){
            Item item = itemRepository.findById(orderReqDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);
        return order.getId();
    }

}
