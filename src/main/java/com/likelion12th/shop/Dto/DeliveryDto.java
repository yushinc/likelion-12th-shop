package com.likelion12th.shop.Dto;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DeliveryDto {

    private Integer amount;
    private Integer price;
    private String customerId;
    private String deliveryStatus;
}
