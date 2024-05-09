package com.likelion12th.shop.controller;


import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<Long> createItem(@RequestPart(name="itemFormDto") ItemFormDto itemFormDto, @RequestPart(value="itemImg", required = false) MultipartFile itemImg){
        if(itemImg==null){
            try{
                Long itemId=itemService.saveItem(itemFormDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }else{
            try{
                Long itemId=itemService.saveItem(itemFormDto, itemImg);

                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);

            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
    @GetMapping("")
    public ResponseEntity<List<ItemFormDto>> getItems(){
        return ResponseEntity.ok().body(itemService.getItems());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId){
        try{
            ItemFormDto itemFormDto = itemService.getItemById(itemId);

            return ResponseEntity.ok().body(itemFormDto);

        } catch (HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemFormDto>> searchItemsByName(@RequestParam("itemName") String itemName) {
        try {
            //상품명으로 상품 조회
            List<ItemFormDto> itemFormDtos = itemService.getItemsByName(itemName);
            //조회된 상품 정보 반환
            return ResponseEntity.ok().body(itemFormDtos);
        } catch (Exception e) {
            //예외 발생 시 500 에러 반한
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long itemId, @RequestPart(name="itemFormDto") ItemFormDto itemFormDto, @RequestPart(value="itemImg", required = false) MultipartFile itemImg){

        try{
            //상품 수정
            itemService.updateItem(itemId,itemFormDto,itemImg);
            return ResponseEntity.ok().body("상품이 성공적으로 수정되었습니다.");

        }catch(HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body("상품을 찾을 수 없습니다.");
        }catch(Exception e){
            //그 외 예외 발생 시 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId){
        try{
            itemService.deleteItem(itemId);
            return ResponseEntity.ok().body("상품이 성공적으로 삭제되었습니다.");
        }catch(HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body("상품을 찾을 수 없습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
