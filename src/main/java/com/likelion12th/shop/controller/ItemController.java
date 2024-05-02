package com.likelion12th.shop.controller;

import com.likelion12th.shop.Dto.ItemFormDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.likelion12th.shop.service.ItemService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new") // PostMapping + api/item/new까지 붙으면 아이템등록 로직을 수행하는것 위에꺼는 그냥 다 묶어서 처리하는 공통로직이고
    public ResponseEntity<Long> createItem(@RequestPart(name = "itemForm") ItemFormDto itemFormDto,
                                           @RequestPart(value = "itemImg", required = false) MultipartFile itemImg) {
        if (itemImg == null) {
            try {
                Long itemId = itemService.saveItem(itemFormDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            try {
                //새로운 아이템 저장
                Long itemId = itemService.saveItem(itemFormDto, itemImg);
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemFormDto>> findAllItems(){
        return ResponseEntity.ok().body(itemService.findAllItems());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId) {
        try {
            // 아이템 ID를 사용하여 특정 상품 조회
            ItemFormDto itemFormDto = itemService.getItemById(itemId);
            // 조회된 상품 정보 변환
            return ResponseEntity.ok().body(itemFormDto);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






    //postman post 보낼때 실행 중인 상태에서 들어가서 send 눌러야함.
    // key 이름은 위에 @RequestPart(name="itemForm") 이거랑 동일해야함. 다르면 안됨!



}
