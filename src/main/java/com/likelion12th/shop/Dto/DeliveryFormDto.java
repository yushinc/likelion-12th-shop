package com.likelion12th.shop.Dto;

import com.likelion12th.shop.entity.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DeliveryFormDto {

    private Integer deliveryAmount;
    private Integer deliveryPrice;
    private String deliveryStatus;
}
