package com.likelion12th.shop.repository;


import com.likelion12th.shop.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemName(String itemName);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //상품 키워드를 통해 List<Item>을 반환하는 쿼리 메서드 추가
    //findBy : 지정된 조건에 따라 엔티티를 찾는 메서드를 선언하는 것을 의미
    //ItemName : "상품명"을 나타내는 엔티티의 속성
    //Containing : 해당 문자열을 포함하는 것을 나타냄
    //IgnoreCase : 대소문자를 구분하지 않음을 나타냄
    List<Item> findByItemNameContainingIgnoreCase(String itemName);


    @Query("select i from Item i where i.itemDetail like "+"%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);



    //@Query-nativeQuery 속성 예제
//    @Query(value = "select  * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
//    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
