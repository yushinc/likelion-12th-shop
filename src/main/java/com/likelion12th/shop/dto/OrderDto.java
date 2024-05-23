package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<Long> itemIds;
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    public static OrderDto of(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setItemIds(order.getOrderItemList().stream()
                .map(orderItem -> orderItem.getItem().getId())
                .collect(Collectors.toList()));
        return orderDto;

    }
}
