package com.likelion12th.shop.controller;

import com.likelion12th.shop.dto.CartDetailDto;
import com.likelion12th.shop.dto.CartItemDto;
import com.likelion12th.shop.dto.CartOrderDto;
import com.likelion12th.shop.entity.CartItem;
import com.likelion12th.shop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addCartItems(@RequestParam String email, @RequestBody List<CartItemDto> cartItemDtos) {
        cartService.addCartItems(email, cartItemDtos);
        return ResponseEntity.ok("장바구니에 상품을 담았음");
    }

    @GetMapping
    public ResponseEntity<List<CartDetailDto>> getCart(@RequestParam String email) {
        List<CartDetailDto> cartDetailDto = cartService.getCartList(email);
        return ResponseEntity.ok(cartDetailDto);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@RequestParam String email, @PathVariable Long cartItemId){
        try{
            cartService.deleteCartItem(email, cartItemId);
            return ResponseEntity.ok("장바구니에서 상품 삭제됨 : " + cartItemId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품 ID 없음 : " + cartItemId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 사용자의 장바구니에 없는 상품임");
        }
    }

    @PatchMapping("")
    public ResponseEntity<String> updateCartItemCount(@RequestParam String email, @RequestBody CartDetailDto cartDetailDto) {
        try {
            cartService.updateCartItemCount(email, cartDetailDto);
            return ResponseEntity.ok("상품 ID : " + cartDetailDto.getCartItemId() + ", 수량 : " + cartDetailDto.getCount());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품 ID가 없음");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 사용자의 장바구니에 없는 상품임");
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<String> orderCartItem(@RequestParam String email, @RequestBody CartOrderDto cartOrderDto) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        try {
            Long orderId = cartService.orderCartItem(email, cartOrderDtoList);

            return ResponseEntity.ok("사용자 : " + email + ", 주문 : " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 실패 : " + e.getMessage());
        }
    }
}
