package com.likelion12th.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
//역할: 어노테이션으로 기본 생성자 자동 추가 수량 변경 시 일부 필드만 포함하는 json데이터로부터 객체를 생성해야하기 때문에 기본 생성자 필요
public class CartDetailDto {
    private Long cartItemId;
    private String itemName;
    private int itemPrice;
    private int count;
    private String itemImgPath;

    public CartDetailDto(Long cartItemId, String itemName, int itemPrice, int count, String itemImgPath){
        this.cartItemId=cartItemId;
        this.itemName=itemName;
        this.itemPrice=itemPrice;
        this.count=count;
        this.itemImgPath=itemImgPath;
    }
}
