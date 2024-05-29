package com.likelion12th.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemDto {
    private Long itemId;
    private int count;
}
