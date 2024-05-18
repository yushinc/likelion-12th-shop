package com.likelion12th.shop.Dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class OrderDto {
    private Long orderId; // 주문 아이디
    private String orderDate;
    private OrderStatus orderStatus;
    private Long itemId;
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    // Order 객체를 OrderDto로 변환
    public static OrderDto of(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        if (!order.getOrderItems().isEmpty()) {
            orderDto.setItemId(order.getOrderItems().get(0).getItem().getId());
        }
        return orderDto;
    }
}
