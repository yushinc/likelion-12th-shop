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
import org.modelmapper.internal.bytebuddy.pool.TypePool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
@RequiredArgsConstructor
//해당 클래스 내에 final 필드로 선언된 repository의 생성자를 자동으로 생성해주는 어노테이션
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    //주문하기
    //주문 요청에 대한 정보를 포함하는 Dto와 주문을 생성하는 데
    //필요한 회원의 이메일 주소를 받아오도록 작성
    //Return 값은 주문 id
    public Long createNewOrder(OrderReqDto orderReqDto, String email) {
        //이메일로 회원 조회
        Member member=memberRepository.findByEmail(email);
        //회원이 존재하지 않을 경우 email로 새로운 회원 생성
        if(member==null){
            Member newMember=new Member();
            //member 객체 생성
            
            newMember.setEmail(email);
            //새로 생성한 member 객체에 이메일 설정
            
            member = memberRepository.save(newMember);
            //새로 생성한 member객체를 데이터베이스에 저장

        }

        //orderReqDto에서 상품 ID를 가져온다.
        Long itemId= orderReqDto.getItemId();

        //상품 ID로 상품 정보를 데이터베이스에서 조회
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new IllegalArgumentException("상품 ID 없음 : "+itemId));


        //주문 상품 생성(상품과 주문 개수)
        //hint! 주문할 상품과 수량으로 OrderItem 객체를 만드는 메소드를 호출
        OrderItem orderItem= OrderItem.createOrderItem(item,orderReqDto.getCount());

        //주문 상품 목록과 회원 정보로 주문을 생성한다.
        List<OrderItem> orderItems=new ArrayList<>();
        orderItems.add(orderItem);

        //hint! 회원과 아이템 리스트로 주문 생성하는 메소드를 호출
        Order order = Order.createOrder(member, orderItems);

        //생성된 주문을 저장
        orderRepository.save(order);

        //생성된 주문의 ID 반환
        return order.getId();
    }

    //모든 주문 내역 조회
    public List<OrderDto> getAllOrdersByUserEmail(String email){
        List<Order> orders=orderRepository.findByMemberEmail(email);

        List<OrderDto> OrderDtos=new ArrayList<>();
        orders.forEach(s-> OrderDtos.add(OrderDto.of(s)));
        return OrderDtos;

    }

    //주문 상세 조회
    public OrderItemDto getOrderDetails(Long orderId, String email){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문 ID 없음 : "+orderId));

        if (!order.getMember().getEmail().equals(email)){
            throw new IllegalArgumentException("주문자만 접근 가능함");
        }
        List<OrderItem> orderItems=order.getOrderItemList();

        if(!orderItems.isEmpty()){
            OrderItem orderItem = orderItems.get(0);

            return OrderItemDto.of(orderItem);
        } else{
            throw new IllegalArgumentException("주문 아이템이 없음");
        }
    }

    //주문 취소
    public void cancelOrder(Long orderId, String email){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문 ID 없음 : "+orderId));

        if(!order.getMember().getEmail().equals(email)){
            throw new IllegalArgumentException("취소 권한이 없음");
        }
        order.cancelOrder();
        orderRepository.save(order);



    }



}
