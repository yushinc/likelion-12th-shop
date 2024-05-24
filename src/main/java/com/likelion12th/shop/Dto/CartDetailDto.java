package com.likelion12th.shop.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDetailDto {  // 장바구니 상품 정보

    private Long cartItemId; // 장바구니 상품 아이디

    private String itemName; //상품명

    private int itemPrice; // 상품 금액

    private int count; // 수량

    private String itemImgPath; // 상품 이미지 경로

    public CartDetailDto(Long cartItemId, String itemName, int itemPrice, int count, String itemImgPath){
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemImgPath = itemImgPath;
    }
}
