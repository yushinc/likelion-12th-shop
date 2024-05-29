package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private int totalPrice;
    private List<Long> itemIds;


    private static ModelMapper modelMapper = new ModelMapper();

    public static OrderDto of(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
//        if(!order.getOrderItem().isEmpty()) {
//            orderDto.setItemId(order.getOrderItem().get(0).getItem().getId());
//        }
        // Order 객체의 orderItemList를 스트림의 변환하고,
        // orderDto의 itemIds 필드에 설정
        orderDto.setItemIds(order.getOrderItem().stream().map(orderItem -> orderItem.getItem().getId()).collect(Collectors.toList()));
        return orderDto;
    }

}
