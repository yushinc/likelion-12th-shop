package com.likelion12th.shop.service;

import com.likelion12th.shop.Dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    // 아이템 등록을 위해 사용할 레포지토리 - 의존성 주입(필드주입)
    @Autowired
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception {
        // 상품 등록
        Item item = itemFormDto.createItem(); // ItemFormDto를 이용하여 Item 객체 생성
        itemRepository.save(item); // 생성한 Item 저장
        return item.getId(); // 저장된 Item의 ID 반환
    }

    @Value("${uploadPath}")   //import lombok이 아닌 beans.factory로 설정되어야함 주의!
    private String uploadPath;
    public  Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
        //상품 등록
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

    public List<ItemFormDto> findAllItems() {
        List<Item> items = itemRepository.findAll(); // 모든 상품 조회
        List<ItemFormDto> itemFormDtos = new ArrayList<>(); // 변환된 DTO를 담을 리스트 생성
        items.forEach(item -> itemFormDtos.add(ItemFormDto.of(item))); // 각 상품을 DTO로 변환하여 리스트에 추가
        return itemFormDtos; // DTO 리스트 반환
    }

    public ItemFormDto getItemById(Long itemId) {
        //아이템 ID로 아이템을 조회
        Optional<Item>optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            //아이템을 찾았을 경우 ItemFormDto로 변환하여 반환
            return ItemFormDto.of(optionalItem.get());
        } else {
            //아이템을 찾지 못했을 경우 예외처리
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }


}






