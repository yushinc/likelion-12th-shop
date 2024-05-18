package com.likelion12th.shop.Service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long createNewOrder(OrderReqDto orderReqDto,String email ){
        Member member=memberRepository.findByEmail(email);
        if (member ==null){
            Member newmember=new Member();
            newmember.setEmail(email);
            member = memberRepository.save(newmember);
        }
        Long itemId=orderReqDto.getItemId();
        Item item=itemRepository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("상품 ID 없음 : "+itemId));
        OrderItem orderItem=OrderItem.createOrderItem(item,orderReqDto.getCount());

        List<OrderItem> orderItems=new ArrayList<>();
        orderItems.add(orderItem);
        Order order=Order.createOrder(member,orderItems);
        orderRepository.save(order);

        return order.getId();
    }
    public List<OrderDto>getAllOrdersByUserEmail(String email){
        List<Order>orders=orderRepository.findByMemberEmail(email);
        List<OrderDto> OrderDtos=new ArrayList<>();
        orders.forEach(s -> OrderDtos.add(OrderDto.of(s)));

        return OrderDtos;
    }
    public OrderItemDto getOrderDetails(Long orderId, String email){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문 ID 없음 : "+orderId));
        if(!Objects.equals(order.getMember().getEmail(), email)){
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }
        List<OrderItem>orderItems=order.getOrderItemlist();
        if(!orderItems.isEmpty()){
            OrderItem orderItem=orderItems.get(0);


            return OrderItemDto.of(orderItem);
        }else {
            throw new IllegalArgumentException("주문 아이템이 없음");
        }
    }
    public void canelOrder(Long orderId, String email){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문 ID 없음 : "+orderId));
        if(!order.getMember().getEmail().equals(email)){
            throw new IllegalArgumentException("취소 권한이 없음");
        }
        //cancel로 주문 상태 변경, 재고 수량 증가
        order.cancelOrder();

        orderRepository.save(order);


    }
}
