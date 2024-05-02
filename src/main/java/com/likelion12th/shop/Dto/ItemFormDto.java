package com.likelion12th.shop.Dto;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemFormDto {

    private  Long id;
    @NotNull
    private String itemName;
    @NotNull
    private Integer price;
    @NotNull
    private String itemDetail;
    @NotNull
    private Integer stock;
    @NotNull
    private ItemSellStatus itemSellStatus;

    // get 에서만 사용
    private String itemImgPath;

    public static ModelMapper modelMapper = new ModelMapper();
    // Dto -> 엔티티 객체 변환을 위한 메서드
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
}
