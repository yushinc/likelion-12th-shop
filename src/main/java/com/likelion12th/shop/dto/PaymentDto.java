package com.likelion12th.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDto {
    private Integer amount;
    private Integer price;
    private String paymentMethod;
    private String customerId;
}

