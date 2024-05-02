package com.likelion12th.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFormDto {
    private String itemName;
    private int price;
    private int stock;
    private String itemDetail;
}
