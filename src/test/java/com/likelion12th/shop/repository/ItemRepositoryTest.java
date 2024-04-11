package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")

class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 생성 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("연필");
        item.setPrice(1000);
        item.setStock(10);
        item.setItemDetail("잘 써지는 연필");

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }
}