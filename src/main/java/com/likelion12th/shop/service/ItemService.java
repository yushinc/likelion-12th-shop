package com.likelion12th.shop.service;

import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.likelion12th.shop.entity.Item.item;
import static java.util.Optional.of;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception {
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        return item.getId();
    }

    @Value("${uploadPath}")
    private String uploadPath;

    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {

        Item item = itemFormDto.createItem();
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
        File itemImgFile = new File(uploadPath, fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath + "/" + fileName);
        itemRepository.save(item);

        return item.getId();
    }

    public List<ItemFormDto> getItems() {

        List<Item> items = itemRepository.findAll();
        List<ItemFormDto> itemFormDtos = new ArrayList<>();

        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        return itemFormDtos;
    }

    public ItemFormDto getItemById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isPresent()) {
            return ItemFormDto.of(optionalItem.get());

        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다.: " + itemId);
        }
    }

    public List<ItemFormDto> getItemsByName(String itemName){
    //쿼리메소드 호출하여 해당하는 아이템 리스트 반환하여 저장
    List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(itemName);
    //엔티티 리스트 -> 디티오 리스트로 변경 시 담을 디티오 리스트 선언
    List<ItemFormDto> itemFormDtos = new ArrayList<>();
    //아이템 리스트를 순회하며 디티오를 변환하여 선언된 리스트에 담음
    //각 상품 정보를 ItemFormDto로 변환하여 리스트에 추가
    items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
    //디티오 리스트 반환
    return itemFormDtos;

    }

    public void updateItem(Long itemId, ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception{
        //아이템 ID로 아이템을 조회 - NPE 방지를 위한 객체 포장
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if(optionalItem.isPresent()){
            Item item = optionalItem.get();

            if (itemFormDto.getItemName()!=null){
                item.setItemName(itemFormDto.getItemName());
            }
            if(itemFormDto.getPrice()!=null){
                item.setPrice(itemFormDto.getPrice());
            }
            if(itemFormDto.getItemDetail()!=null){
                item.setItemDetail(itemFormDto.getItemDetail());
            }
            if(itemFormDto.getItemSellStatus()!=null){
                item.setItemSellStatus(itemFormDto.getItemSellStatus());
            }
            if(itemFormDto.getStock()!=null){
                item.setStock(itemFormDto.getStock());
            }
            if(itemImg !=null){
                UUID uuid = UUID.randomUUID();
                String fileName=uuid.toString()+"_"+itemImg.getOriginalFilename();
                File itemImgFile=new File(uploadPath, fileName);
                itemImg.transferTo(itemImgFile);
                item.setItemImg(fileName);
                item.setItemImgPath(uploadPath+"/"+fileName);
            }
            itemRepository.save(item);
        }else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: "+ itemId);
        }

    }
    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isPresent()) {
            itemRepository.delete(optionalItem.get());

        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }


}