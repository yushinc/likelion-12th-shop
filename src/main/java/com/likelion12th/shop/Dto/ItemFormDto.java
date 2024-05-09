package com.likelion12th.shop.Dto;

import com.likelion12th.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemFormDto {
    // 필드
    private Long id;
    @NotNull
    private String itemName;
    private Integer price;
    @NotNull
    private Integer stock;
    @NotNull
    private String itemSellStatus;
    @NotNull
    private String itemDetail;

    //get에서만 사용
    private String itemImgPath;


    // modelMapper  객체 생성
    private static ModelMapper modelMapper = new ModelMapper();

    // Dto -> 엔티티 객체 변환을 위한 메서드
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    // 엔티티 -> Dto 객체 변환을 위한 메서드
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}





