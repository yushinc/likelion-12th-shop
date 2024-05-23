package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.SellStatus;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
        item.setItemDetail("테스트 상품 상새 설명");
        item.setItemSellStatus(SellStatus.SELL);
        item.setStock(100);
        item.setCreatedBy(LocalDateTime.now());
        item.setModifiedBy(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    public void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품 " + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setItemSellStatus(SellStatus.SELL);
            item.setStock(100);
            item.setCreatedBy(LocalDateTime.now());
            item.setModifiedBy(LocalDateTime.now());

            Item saveItem = itemRepository.save(item);
        }

    }
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameList(){
        this.createItemList();
        List<Item> itemList=itemRepository.findByItemName("테스트 상품 5");
        for(Item item:itemList)System.out.println(item.toString());
}
    @Test
    @DisplayName("가격 내림차순 조회 테스트")

    public void findByPriceLessThanOrderByPriceDescTest(){
        this.createItemList();
        List<Item> pricelist=itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item:pricelist){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한  상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> detaillist=itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item:detaillist)System.out.println(item.toString());
    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDsltest(){
        this.createItemList();
        JPAQueryFactory queryFactory=new JPAQueryFactory(em);
        //jpaqeryfactory를 통해 쿼리를 동적으로 생성
        QItem qItem=QItem.item;

        JPAQuery<Item> query=queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(SellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"상세 설명 5"+"%"))
                .orderBy(qItem.price.desc());
        List<Item>itemList=query.fetch();
        for(Item item:itemList){
            System.out.println(item.toString());
        }

        
    }
    }

