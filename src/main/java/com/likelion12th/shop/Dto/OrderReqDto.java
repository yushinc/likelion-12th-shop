package com.likelion12th.shop.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//주문을 생성할때 필요한 정보를 담음
public class OrderReqDto {
    private Long itemId;
    private Integer count;
}
