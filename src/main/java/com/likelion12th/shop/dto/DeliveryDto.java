package com.likelion12th.shop.dto;

import com.likelion12th.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryDto {
    private Member member;
    private String waybillNumber;
}
