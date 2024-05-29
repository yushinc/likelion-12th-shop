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
    //상품 저장 로직 수행=>컨트롤러에서 호출될 예정

    private final ItemRepository itemRepository;
    //아이템 등록을 위해 사용할 레포지토리-의존성주입
    //final : 값을 딱 한번만 할당할 수 있다

    public Long saveItem(ItemFormDto itemFormDto) throws Exception {
        //이미지 파일을 받지 않는 경우

        Item item = itemFormDto.createItem();
        //DTO->엔티티로 객체 변환을 위한 메서드 호출

        itemRepository.save(item);
        //의존성 주입된 레포지토리를 통해 상품 저장

        return item.getId();
        //저장된 상품의 Id값을 반환(성공적으로 저장된 경우)
    }

    @Value("${uploadPath}")
    private String uploadPath;

    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception {
    //이미지 파일을 받는 경우 MultipartFile

        Item item = itemFormDto.createItem();

        //여기부터
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
        File itemImgFile = new File(uploadPath, fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath + "/" + fileName);
        //여기까지 이미지 업로드 로직

        itemRepository.save(item);

        return item.getId();
    }

    public List<ItemFormDto> getItems() {
    // 모든 아이템 리스트 반환

        List<Item> items = itemRepository.findAll();
        //DB에서 모든 Item 리스트를 조회하여 Item 타입의 리스트에 저장

        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        //모든 아이템을 순회하며 itemFormDto 타입으로 변경하여 itemDtos에 저장

        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        //배열의 요소를 간편하게 반족 접근하여 같은 동작을 수행하는 forEach문

        return itemFormDtos;
    }

    public ItemFormDto getItemById(Long itemId) {
        //특정 상품 조회
        //아이템 ID로 아이템을 조회

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        //Optional 객체를 사용하면 예상치 못한 NullPointerException 예외를 제공하는 메소드를 회피할 수 있다.
        //객체에 null 값이 들어가는 것을 미리 방지해줄 수 있음

        if (optionalItem.isPresent()) {
            //아이템을 찾았을 경우 ItemFormDto로 변환하여 반환
            return ItemFormDto.of(optionalItem.get());

        } else {
            //아이템을 찾지 못했을 경우 예외 처리
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다.: " + itemId);
        }
    }

    //상품명으로 상품 조회
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
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        //아이템 ID로 아이템을 조회 - NPE 방지를 위한 객체 포장

        if(optionalItem.isPresent()){
            Item item = optionalItem.get();
            //업데이트할 필드만 업데이트

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

            //이미지 업데이트
            if(itemImg !=null){
                UUID uuid = UUID.randomUUID();
                String fileName=uuid.toString()+"_"+itemImg.getOriginalFilename();
                File itemImgFile=new File(uploadPath, fileName);
                itemImg.transferTo(itemImgFile);
                item.setItemImg(fileName);
                item.setItemImgPath(uploadPath+"/"+fileName);
            }

            //수정된 상품 저장
            itemRepository.save(item);
        }else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: "+ itemId);
        }

    }
    
    //상품 삭제
    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        //삭제할 상품을 아이디로 조회

        if (optionalItem.isPresent()) {
            //상품이 존재하면 삭제
            itemRepository.delete(optionalItem.get());

        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }


}