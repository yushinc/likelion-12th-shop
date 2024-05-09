package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 상품명으로 상품 조회
    List<Item> findByItemNameContainingIgnoreCase(String itemName);

    List<Item> findByItemName(String itemName);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

        // 상품 상세 조회 + 가격 내림차순 정렬
        @Query("select i from Item i where i.itemDetail like" +
                "%:itemDetail% order by i.price desc")
        List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
