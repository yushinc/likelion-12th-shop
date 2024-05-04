package com.likelion12th.shop.repository;


import com.likelion12th.shop.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemName(String itemName);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like "+"%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    List<Item> findByItemDetailByNative(String 테스트_상품_상세_설명);

    //@Query-nativeQuery 속성 예제
//    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
//    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
