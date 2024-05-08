package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.ItemStatus;
import com.likelion12th.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemFormDto {
    private Long itemId;

    @NotNull
    private String itemName;
    @NotNull
    private int price;
    @NotNull
    private int stock;
    @NotNull
    private String itemDetail;
    @NotNull
    private ItemStatus itemSellStatus;

    // get에서만 사용
    private String itemImgPath;

    private static ModelMapper modelMapper = new ModelMapper();
    // Dto -> Entity
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
