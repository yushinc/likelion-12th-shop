package com.likelion12th.shop.controller;

import com.likelion12th.shop.Service.ItemService;
import com.likelion12th.shop.dto.ItemFormDto;
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
        if (itemImg == null)//itemImg가 비어있는 경우
        {
            try {
                Long itemId = itemService.saveItem(itemFormDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            try {
                Long itemId = itemService.saveItem(itemFormDto, itemImg);
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }
    @GetMapping
    public ResponseEntity<List<ItemFormDto>> getItems(){
        return ResponseEntity.ok().body(itemService.getItem());
    }
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId){
        try {
            ItemFormDto itemFormDto=itemService.getItemById(itemId);
            return ResponseEntity.ok().body(itemFormDto);
        }catch (HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
