package com.likelion12th.shop.service;

import com.likelion12th.shop.Dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;


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
    // 원래 @Autowired 써야하지만 위에 @RequiredArgsConstructor 어노테이션 선언했으므로 생략해도 됨.
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception {
        // 상품 등록
        Item item = itemFormDto.createItem(); // ItemFormDto를 이용하여 Item 객체 생성
        itemRepository.save(item); // 생성한 Item 저장
        return item.getId(); // 저장된 Item의 ID 반환
    }

    @Value("${uploadPath}")   //import lombok이 아닌 beans.factory로 설정되어야함 주의!
    private String uploadPath;

    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
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

    //여기 메서드 잘못선언해서 오류났던거임...
    public List<ItemFormDto> getItems() {
        List<Item> items = itemRepository.findAll(); // 모든 상품 조회
        List<ItemFormDto> itemFormDtos = new ArrayList<>(); // 변환된 DTO를 담을 리스트 생성
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s))); // 각 상품을 DTO로 변환하여 리스트에 추가
        return itemFormDtos; // DTO 리스트 반환
    }

    public ItemFormDto getItemById(Long itemId) {
        //아이템 ID로 아이템을 조회
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            //아이템을 찾았을 경우 ItemFormDto로 변환하여 반환
            return ItemFormDto.of(optionalItem.get());
        } else {
            //아이템을 찾지 못했을 경우 예외처리
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }

    //상품 수정
    public void updateItem(Long itemId, ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
        //아이템 ID로 아이템을 조회 - NPE 방지를 위한 객체 포장
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            //업데이트 할 필드만 업데이트
            //item entity에 만들었던 인자들 불러오기. 비어있지 않다면 각각 값들 출력할것.
            if (itemFormDto.getItemName() != null) {
                item.setItemName(itemFormDto.getItemName());
            }
            if (itemFormDto.getPrice() != null) {
                item.setPrice(itemFormDto.getPrice());
            }
            if (itemFormDto.getItemDetail() != null) {
                item.setItemDetail(itemFormDto.getItemDetail());
            }
            if (itemFormDto.getItemSellStatus() != null) {
                item.setItemSellStatus(itemFormDto.getItemSellStatus());
            }
            if (itemFormDto.getStock() != null) {
                item.setStock(itemFormDto.getStock());
            }

            if (itemImg != null) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
                File itemImgFile = new File(uploadPath, fileName);
                itemImg.transferTo(itemImgFile);
                item.setItemImg(fileName);
                item.setItemImgPath(uploadPath + "/" + fileName);
            }
            //수정된 상품 저장
            itemRepository.save(item);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다." + itemId);
        }
    }


    public List<ItemFormDto> getItemsByName(String itemName) {
        //쿼리메소드 호출하여 해당하는 아이템 리스트 반환하여 저장
        // findByItem 쿼리를 사용하면 아예 똑같은 itemName만 반환. 특정단어 포함 itemName 반환하려고 우리가 itemRepository에 findByItemNameContainingIgnoreCas 쿼리 만들었던 것. 그걸 불러와야지!
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(itemName);
        //엔티티 리스트 -> 디티오 리스트로 변경 시 담을 디티오 리스트 선언
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        //아이템 리스트를 순회하며 디티오로 변환하여 선언된 리스트에 담음
        // 각 상품 정보를 ItemFormDto로 변환하여 리스트에 추가
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        return itemFormDtos;
    }

    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isPresent()) {
            itemRepository.delete(optionalItem.get());

        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다:" + itemId);
        }
    }
}





