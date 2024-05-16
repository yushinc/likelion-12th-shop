package com.likelion12th.shop.service;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.dto.MemberFormDto;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final MemberRepository memberRepository;

    public Long createNewOrder(OrderReqDto orderReqDto, String email) {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            Member member1 = new Member();
            member1.setEmail(email);
            member=memberRepository.save(member1);
        }

        Long itemId = orderReqDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("상품 ID 없음 : " + itemId));

        OrderItem orderItem = OrderItem.createOrderItem(item, orderReqDto.getCount());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.createOrder(member, orderItems);

        orderRepository.save(order);

        return order.getId();

    }

    public List<OrderDto> getAllOrdersByUserEmail(String email) {
        List<Order> orders = orderRepository.findByMemberEmail(email);

        List<OrderDto> OrderDtos = new ArrayList<>();

        orders.forEach(s -> OrderDtos.add(OrderDto.of(s)));

        return OrderDtos;
    }

    public OrderItemDto getOrderDetails(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if(!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }

        List<OrderItem> orderItems = order.getOrderItemList();

        if(!orderItems.isEmpty()) {
            OrderItem orderItem = orderItems.get(0);

            return OrderItemDto.of(orderItem);
        } else {
            throw new IllegalArgumentException("주문 아이템이 없음");
        }

    }

    public void cancelOrder(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if(!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("취소 권한이 없음");
        }

        order.cancelOrder();
        orderRepository.save(order);
    }
}
