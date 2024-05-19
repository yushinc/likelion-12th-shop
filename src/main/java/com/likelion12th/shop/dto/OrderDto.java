package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor //매개변수를 가지지 않는 생성자를 자동으로 생성해주는 역할
public class OrderDto {
    private Long orderId; //주문아이디
    private String orderDate; //주문 날짜
    private OrderStatus orderStatus; //주문 상태
    private Long itemId;
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();
    //자바 객체 간의 매핑을 하기 위해 ModelMapper을 선언

    //Order 객체를 OrderDto로 변환
    public static OrderDto of(Order order) {
        //Order 객체를 OrderDto로 변환하는 of 메서드를 정의
        //이는 ModelMapper를 사용하여 order객체를 OrderDto객체로 매핑

        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        if(!order.getOrderItemList().isEmpty()){
            orderDto.setItemId(order.getOrderItemList().get(0).getItem().getId());
        }
        return orderDto;
    }
}
