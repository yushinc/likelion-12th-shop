package com.likelion12th.shop.service;


import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.sql.DriverManager.println;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    private final ItemRepository itemRepository;

    // 이미지 없이 상품 등록
    public Long saveItem(ItemFormDto itemFormDto) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();

        return item.getId();
    }

    // 이미지와 함께 상품 등록
    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();

        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() +"_"+ itemImg.getOriginalFilename();
        File itemImgFile = new File(uploadPath, fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath+"/"+fileName);
        itemRepository.save(item);

        return item.getId();
    }

    // 모든 상품 가져오기
    public List<ItemFormDto> getItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        return  itemFormDtos;
    }

    // 특정 상품 가져오기
    public ItemFormDto getItemById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if(optionalItem.isPresent()){
            return ItemFormDto.of(optionalItem.get());
        }else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수없습니다: " + itemId);
        }
    }

    // 상품명으로 상품 조회
    public List<ItemFormDto> getItemsByName(String itemName){
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(itemName);
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        items.forEach(item -> itemFormDtos.add(ItemFormDto.of(item)));
        return itemFormDtos;
    }

    // 상품 수정
    public void updateItem(Long itemId, ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
        Optional<Item> optionalItem =itemRepository.findById(itemId);

        if(optionalItem.isPresent()){
            Item item = optionalItem.get();

            // 항목 별 null 검사
            if(itemFormDto.getItemName() != null){
                item.setItemName(itemFormDto.getItemName());
            }
            if (itemFormDto.getPrice() != null) {
                item.setPrice(itemFormDto.getPrice());
            }
            if(itemFormDto.getItemDetail() != null){
                item.setItemDetail(itemFormDto.getItemDetail());
            }
            if (itemFormDto.getItemSellStatus() != null) {
                item.setItemSellStatus(itemFormDto.getItemSellStatus());
            }
            if (itemFormDto.getStock() != null) {
                item.setStock(itemFormDto.getStock());
            }
            if(itemImg != null){
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
                File itemImgFile = new File(uploadPath, fileName);
                itemImg.transferTo(itemImgFile);
                item.setItemImg(fileName);
                item.setItemImgPath(uploadPath + "/" +fileName);
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }

    // 상품 삭제
    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if(optionalItem.isPresent()){
            itemRepository.delete(optionalItem.get());
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다.");
        }
    }
}
