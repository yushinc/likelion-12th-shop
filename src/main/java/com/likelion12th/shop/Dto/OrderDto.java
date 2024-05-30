package com.likelion12th.shop.Dto;


import java.util.List;
import java.util.stream.Collectors;

import com.likelion12th.shop.constant.OrderStatus;
import com.likelion12th.shop.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<Long> itemIds; // 여러 아이템 ID
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    //Order 객체를 OrderDto로 변환
    public static OrderDto of(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);

        // Order 객체의 orderItemList를 스트림의 변환하고,
        // orderDto의 itemIds 필드에 설정
        orderDto.setItemIds(order.getOrderItems().stream()
                // orderItem 객체에서 itemId 추출
                .map(orderItem -> orderItem.getItem().getId())
                // itemId를 리스트로
                .collect(Collectors.toList()));
        return orderDto;
    }


}

