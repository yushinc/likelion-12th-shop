package com.likelion12th.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartOrderDto { // 장바구니에 담은 상품 주문 시 전달할 dto
    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtosList;
}
