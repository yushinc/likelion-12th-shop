package com.likelion12th.shop.Dto;

import com.likelion12th.shop.constant.ItemSellStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ItemFormDto {
    private String itemName;
    private String price;
    private Integer stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;
}
