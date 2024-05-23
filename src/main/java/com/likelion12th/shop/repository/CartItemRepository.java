package com.likelion12th.shop.repository;

import com.likelion12th.shop.dto.CartDetailDto;
import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.CartItem;
import com.likelion12th.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long CartId, Long itemId);

    @Query("SELECT new com.likelion12th.shop.dto.CartDetailDto(ci.id, ci.item.itemName, ci.item.price, ci.count, ci.item.itemImgPath)" +
        "FROM CartItem ci " + //ci뒤에 띄어쓰기 꼭 하기
        "WHERE ci.cart.id = :cartId ")
    List<CartDetailDto>findCartOrderList(@Param("cartId") Long cartId);
}
