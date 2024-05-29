package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor //매개변수를 가지지 않는 생성자를 자동으로 생성해주는 역할
public class OrderDto {
    private Long orderId; //주문아이디
    private String orderDate; //주문 날짜
    private OrderStatus orderStatus; //주문 상태
    private List<Long> itemIds; //여러 아이템 ID
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();
    //자바 객체 간의 매핑을 하기 위해 ModelMapper을 선언

    //Order 객체를 OrderDto로 변환
    public static OrderDto of(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);

        orderDto.setItemIds(order.getOrderItemList().stream()
                .map(orderItem -> orderItem.getItem().getId())
                .collect(Collectors.toList()));
        return orderDto;
    }
}
