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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    // item 등록에 사용할 레포지토리
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception{
        Item item = itemFormDto.createItem();

        itemRepository.save(item);

        return item.getId();
    }


    @Value("${uploadPath}")
    private String uploadPath;
    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
        Item item = itemFormDto.createItem();

        // 이미지 업로드
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
        File itemImgFile = new File(uploadPath, fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath + "/" + fileName);
        itemRepository.save(item);

        return item.getId();
    }

    public List<ItemFormDto> getItems(){
        // 모든 item 리스트를 조회해서 저장
        List<Item> items = itemRepository.findAll();
        // items를 dto 타입으로 변경 후 반환
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));

        return itemFormDtos;

    }

    public ItemFormDto getItemById(Long itemId) {
        // null 방지를 위해 Optional 클래스로 포장
        Optional<Item> optionalItem =itemRepository.findById(itemId);

        // isPresent: optional 객체가 값을 가지고 있다면 true, 아니면 false
        if(optionalItem.isPresent()) {
            return ItemFormDto.of(optionalItem.get());
        } else {
            throw  new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    public List<ItemFormDto> getItemsByName(String itemName) {
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(itemName);
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        return itemFormDtos;
    }

    public void updateItem(Long itemId, ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if(optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if(itemFormDto.getItemName() != null) {
                item.setItemName(itemFormDto.getItemName());
            }
            if (itemFormDto.getPrice() != null) {
                item.setPrice(itemFormDto.getPrice());
            }
            if (itemFormDto.getItemDetail() != null) {
                item.setItemDetail(itemFormDto.getItemDetail());
            }
            if(itemFormDto.getItemSellStatus() != null) {
                item.setItemSellStatus(itemFormDto.getItemSellStatus());
            }
            if(itemFormDto.getStock() != null) {
                item.setStock(itemFormDto.getStock());
            }
            if(itemImg != null) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
                File itemImgFile = new File(uploadPath, fileName);
                itemImg.transferTo(itemImgFile);
                item.setItemImg(fileName);
                item.setItemImgPath(uploadPath+ "/" + fileName);

            }

            // 상품 저장
            itemRepository.save(item);

        }
        else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }

    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if(optionalItem.isPresent()) {
            itemRepository.delete(optionalItem.get());
        }
        else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }

}
