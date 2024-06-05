package com.likelion12th.shop.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {  // 장바구니 주문
    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;
}
