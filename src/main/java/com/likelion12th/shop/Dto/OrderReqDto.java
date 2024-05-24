package com.likelion12th.shop.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderReqDto {
    private Long itemId;
    private Integer count;
}
