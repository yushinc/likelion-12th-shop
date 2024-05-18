package com.likelion12th.shop.dto;

import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class OrderItemDto {
    private Long itemId;
    private String ItemName;
    private Integer itemPrice;
    private Integer count;
    private int totalPrice;

    private static ModelMapper modelMapper=new ModelMapper();

    public static OrderItemDto of(OrderItem orderItem){
        return modelMapper.map(orderItem,OrderItemDto.class);
    }
}
