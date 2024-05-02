package com.likelion12th.shop.Service;

import com.likelion12th.shop.Dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.likelion12th.shop.Dto.ItemFormDto.modelMapper;
import static com.likelion12th.shop.entity.QItem.item;
import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {


    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception {
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        return item.getId();
    }


    @Value("${uploadPath}")
    private String uploadPath;

    public Long saveItem(ItemFormDto itemFormDto,MultipartFile itemImg) throws Exception{
        // 상품 등록
        Item item = itemFormDto.createItem();
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + itemImg.getOriginalFilename();
        // 파일 객체 생성: 업로드 경로와 파일 이름을 이용하여 새로운 파일 객체 생성
        File itemImgFile = new File(uploadPath,fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath+"/"+fileName);
        itemRepository.save(item);

        return item.getId();
    }

    // 전체 상품 조회
    public List<ItemFormDto> getItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemFormDto> itemFormDtos = new ArrayList<>();
        items.forEach(s-> itemFormDtos.add(ItemFormDto.of(s)));
        return itemFormDtos;
    }

    // 특정 상품 조회
    public ItemFormDto getItemById(Long itemId) {
        // 아이템 ID로 아이템을 조회
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            // 아이템을 찾았을 경우 ItemFormDto로 변환하여 반환
            return ItemFormDto.of(optionalItem.get());
         } else {
            // 아이템을 찾지 못했을 경우 예외 처리
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "id에 해당하는 상품을 찾을 수 없습니다: " + itemId);
        }
    }
}