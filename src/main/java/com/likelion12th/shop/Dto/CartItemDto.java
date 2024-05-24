package com.likelion12th.shop.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto { // 장바구니에 담을 상품의 아이디와 수량
    private Long itemId;
    private int count;
}
