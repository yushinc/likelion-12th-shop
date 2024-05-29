package com.likelion12th.shop.repository;

import com.likelion12th.shop.dto.CartDetailDto;
import com.likelion12th.shop.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 장바구니 조회
    @Query("SELECT new com.likelion12th.shop.dto.CartDetailDto(ci.id, ci.item.itemName, ci.item.price, ci.count, ci.item.itemImgPath) " +
            "FROM CartItem ci " +
            "WHERE ci.cart.id = :cartId")
    List<CartDetailDto> findCartOrderList(@Param("cartId") Long cartId);
}
