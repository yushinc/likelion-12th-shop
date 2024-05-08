package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemName(String itemName);         // 상품명으로 상품 조회하는 쿼리메소드 생성
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer Price);  // price 변수보다 값이 작은 상품 내림*순 정렬


    @Query("select i from Item i where i.itemDetail like " + "%:itemDetail% order by i.price desc ")
    List<Item> findByItemDetailIs(@Param("itemDetail") String itemDetail);


    @Query(value = "select * from item i where i.item_detail like " + "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);


}
