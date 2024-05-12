package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 상품명으로 상품 조회
    List<Item> findByItemName(String itemName);

    // 가격 기준 이하 내림차순 비교
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 이름 포함 상품명 검색
    List<Item> findByItemNameContainingIgnoreCase(String itemName);

    // 상품 설명 검색
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
