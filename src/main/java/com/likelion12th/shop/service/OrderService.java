package com.likelion12th.shop.service;


import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.dto.ItemFormDto;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

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

    public OrderItemDto getOrderDetails(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 ID 없음 : " + orderId));

        if (!order.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }

        List<OrderItem> orderItems = order.getOrderItemList();

        if (!orderItems.isEmpty()) {
            OrderItem orderItem = orderItems.get(0);

            return OrderItemDto.of(orderItem);
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

}
