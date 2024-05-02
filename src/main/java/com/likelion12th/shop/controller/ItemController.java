package com.likelion12th.shop.controller;

import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<Long> createItem(@RequestPart(name = "itemFormDto") ItemFormDto itemFormDto,
                                           @RequestPart(value = "itemImg", required = false) MultipartFile itemImg) {
        if (itemImg == null) {
            try {
                // 새로운 아이템 저장
                Long itemId = itemService.saveItem(itemFormDto);

                // 저장된 아이템의 아이디 반환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                //예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            try {
                // 새로운 아이템 저장
                Long itemId = itemService.saveItem(itemFormDto, itemImg);

                // 저장된 아이템의 아이디 변환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                // 예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @RequestMapping
    public ResponseEntity<List<ItemFormDto>> getItems() {
        return ResponseEntity.ok().body(itemService.getItems());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId) {
        try {
            // 아이템 Id를 사용해 특정 상품 조회
            ItemFormDto itemFormDto = itemService.getItemById(itemId);
            // 조회된 상품 정보 반환
            return ResponseEntity.ok().body(itemFormDto);
        } catch (HttpClientErrorException e) {
            // 아이템 찾지 못할 경우 404 에러 반환
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
