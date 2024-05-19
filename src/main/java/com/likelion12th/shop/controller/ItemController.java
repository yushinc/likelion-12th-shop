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
//객체를 JSON 형태로 변환해 ResponseEntity로 감싸 반환
//처리과정
//1. Client가 URI 형식으로 Server에 Request 전송
//2. Dispather Servlet이 요청을 처리할 대상 탐색
//3. Handler Adapter가 요청한 적절한 Controller로 위힘
//4. Controller가 요청 처리 후 객체 반환
//5. 객체가 JSON 형태로 반환되어 Client에게 전송

@RequestMapping("/api/item")
//특정 uri로 요청을 보내면 Controller에서 어떠한 방식으로 처리할지 정의하는데 이때 들어온 요청을
//메서드와 매핑하기 위해 사용하는 어노테이션

@RequiredArgsConstructor
//롬복으로 스프링에서 의존성 주입 방법 중에 생성자 주입을 임의의 코드 없이 자동으로 설정해주는 어노테이션
//final 필드나 @NonNull이 붙은 필드에 대해 생성자를 생성해준다
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    //클라이언트로부터의 HTTP 요청을 받아들이고 해당 요청에 대한 응답을 반환
    //원하는 요청을 수행할 수 있도록 서비스의 해당 로직을 호출

    public ResponseEntity<Long> createItem(@RequestPart(name="itemFormDto") ItemFormDto itemFormDto, @RequestPart(value="itemImg", required = false) MultipartFile itemImg){
        if(itemImg==null){
            //itemImg가 비어있는 경우
            try{
                // 새로운 아이템 저장
                Long itemId=itemService.saveItem(itemFormDto);

                //저장된 아이템의 아이디 반환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e){
                //예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }else{
            //itemImg가 들어온 경우(MultipartFile 타입의 itemImg)
            try{
                //새로운 아이템 저장-이미지 있는 상품 등록
                Long itemId=itemService.saveItem(itemFormDto, itemImg);

                //저장된 아이템의 아이디 반환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);

            }catch (Exception e){
                //예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
    @GetMapping("")
    //HTTP GET요청에 대한 매피을 정의한다.

    public ResponseEntity<List<ItemFormDto>> getItems(){
        return ResponseEntity.ok().body(itemService.getItems());
    }
    //서비스에게 전달받은 리스트를 클라이언트에게 전달
    //아이템 반환 로직을 호출하여 body에 리턴값(dto리스트)을 담음

    //특정 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId){
        //@PathVatiable: 경로 변수를 표시하기 위해 메서드에 매개변수에 사용
        //경로 변수는 중괄호{id}로 둘러싸인 값을 나타낸다.
        //상세 조회, 수정, 삭제와 같은 작업에서 리소스 식별자로 사용

        try{
            ItemFormDto itemFormDto = itemService.getItemById(itemId);
            //아아템 ID를 사용하여 특정 상품 조회

            return ResponseEntity.ok().body(itemFormDto);
            //조회된 상품 정보 반환

        } catch (HttpClientErrorException e){
            //아이템을 찾지 못한 경우 404 에러 반환
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e){
            //그 외 예외 발생 시 500 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //상품명으로 상품 조회
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

    //기존 상품 수정
    @PatchMapping("/{itemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long itemId, @RequestPart(name="itemFormDto") ItemFormDto itemFormDto, @RequestPart(value="itemImg", required = false) MultipartFile itemImg){

        try{
            //상품 수정
            itemService.updateItem(itemId,itemFormDto,itemImg);
            return ResponseEntity.ok().body("상품이 성공적으로 수정되었습니다.");

        }catch(HttpClientErrorException e){
            //찾을 수 없는 경우 에러 처리
            return ResponseEntity.status(e.getStatusCode()).body("상품을 찾을 수 없습니다.");
        }catch(Exception e){
            //그 외 예외 발생 시 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId){
        try{
            //상품 삭제
            itemService.deleteItem(itemId);
            return ResponseEntity.ok().body("상품이 성공적으로 삭제되었습니다.");
        }catch(HttpClientErrorException e){
            //찾을 수 없는 경우 에러 처리
            return ResponseEntity.status(e.getStatusCode()).body("상품을 찾을 수 없습니다.");
        }catch(Exception e){
            //그 외 예외 발생 시 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
