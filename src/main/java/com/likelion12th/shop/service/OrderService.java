package com.likelion12th.shop.service;

import com.likelion12th.shop.dto.OrderDto;
import com.likelion12th.shop.dto.OrderItemDto;
import com.likelion12th.shop.dto.OrderReqDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Order;
import com.likelion12th.shop.entity.OrderItem;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import com.likelion12th.shop.repository.OrderItemRepository;
import com.likelion12th.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Long createNewOrder(OrderReqDto orderReqDto , String email) {
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            member = new Member();
            member.setEmail(email);
            memberRepository.save(member);
        }

        Long itemId = orderReqDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("상품 ID 없음: " + itemId));

        OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.createOrder(member, orderItems);
        orderRepository.save(order);

        return order.getId();

    }

    public List<OrderDto> getAllOrdersByUserEmail(String email) {
        List<Order> orders = orderRepository.findAllByMemberEmail(email);
        List<OrderDto> orderDtos = new ArrayList<>();

        // order리스트에 있는 각 주문을 반복하면서 orderDto로 변환, 이를 orderDtos 리스트에 추가하는 람다 표현식
        orders.forEach(s -> orderDtos.add(OrderDto.of(s)));

        return orderDtos;
    }

    public OrderItemDto getOrderDetails(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음: " + orderId));

        if (! order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }

        List<OrderItem> orderItems = order.getOrderItem();
        if(! orderItems.isEmpty()) {
            OrderItem orderItem = orderItems.get(0);

            // OrderItemDto 객체로 변환해 반환
            return OrderItemDto.of(orderItem);
        }
        else {
            throw new IllegalArgumentException("주문 아이템이 없음");
        }
    }

    public void cancelOrder(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if (!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("취소 권한이 없음");
        }

        order.cancelOrder();
        orderRepository.save(order);
    }


}
