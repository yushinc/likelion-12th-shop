package com.likelion12th.shop.Service;

import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    @Autowired
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto) throws Exception{
        Item item=itemFormDto.createItem();
        itemRepository.save(item);

        return item.getId();
    }
    @Value("${uploadPath}")
    private String uploadPath;
    public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImg) throws Exception{
        Item item=itemFormDto.createItem();
        UUID uuid =UUID.randomUUID();
        String fileName=uuid.toString()+"_"+itemImg.getOriginalFilename();
        File itemImgFile=new File(uploadPath,fileName);
        itemImg.transferTo(itemImgFile);
        item.setItemImg(fileName);
        item.setItemImgPath(uploadPath+"/"+fileName);
        itemRepository.save(item);

        return item.getId();

    }
    public List<ItemFormDto> getItem(){
        List<Item> items=itemRepository.findAll();
        List<ItemFormDto> itemFormDtos=new ArrayList<>();
        items.forEach(s -> itemFormDtos.add(ItemFormDto.of(s)));
        return itemFormDtos;
    }
    public ItemFormDto getItemById(Long ItemId){
        Optional<Item> optionalItem=itemRepository.findById(ItemId);

        if(optionalItem.isPresent()){
            return ItemFormDto.of(optionalItem.get());
        }
        else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND,"ID에 해당하는 상품을 찾을 수 없습니다."+ItemId);
        }
    }
}
