package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private Long itemId;
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    public static OrderDto of(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        if(!order.getOrderItemList().isEmpty()) {
            orderDto.setItemId(order.getOrderItemList().get(0).getItem().getId());
        }
        return orderDto;

    }
}
