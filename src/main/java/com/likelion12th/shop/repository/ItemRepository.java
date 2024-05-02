package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Item;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {
    //상품명으로 상품 조회
    List<Item> findByItemName(String itemName);

    //상품 상세 설명 조회 + 상품 ID 내림차순 정렬
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    //가격보다 작은 가격을 가진 상품을 내림차순으로 정렬하여 조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

}

