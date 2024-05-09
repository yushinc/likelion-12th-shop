package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class ItemRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }


    private void createItemList() {
        // 10개의 상품 생성 및 저장
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품 " + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세설명 " + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStock(100);
            item.setCreatedBy(LocalDateTime.now());
            item.setModifiedBy(LocalDateTime.now());

            Item savedItem = itemRepository.save(item); // 상품 저장
            System.out.println(savedItem.toString());

            Item savedcreateItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameList() {
        this.createItemList();
        // "테스트 상품"을 상품명으로 갖는 상품을 조회합니다.
        List<Item> itemList = itemRepository.findByItemName("테스트 상품 2");
        // 조회된 상품들을 출력합니다.
        for (int i = 1; i <= itemList.size(); i++) {
            System.out.println(itemList.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        // 가격이 10005원 이하인 상품을 조회하고, 가격을 내림차순으로 정렬하여 출력
        List<Item> PriceList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : PriceList) {
            System.out.println(item.toString());
        }
    }


    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> DetailList = itemRepository.findByItemDetail("테스트 상품 상세설명");

        for (Item item : DetailList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //jpaqeryfactory를 통해 쿼리를 동적으로 생성
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세설명" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println(item.toString());
        }

    }

}










