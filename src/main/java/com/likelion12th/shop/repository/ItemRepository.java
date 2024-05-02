package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.SellStatus;
import com.likelion12th.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>{
    List<Item> findByItemName(String itemName);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    @Query("select i from Item i  where i.itemDetail like " +
            "%:itemDetail% order by i.price desc ") //i가 생각되면 안됨.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


}
