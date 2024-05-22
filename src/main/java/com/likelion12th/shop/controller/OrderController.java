package com.likelion12th.shop.controller;


import com.likelion12th.shop.dto.OrderDto;
import com.likelion12th.shop.dto.OrderItemDto;
import com.likelion12th.shop.dto.OrderReqDto;
import com.likelion12th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<String> createNewOrder(@RequestParam(name = "email") String email, @RequestBody OrderReqDto orderReqDto) {
        try {
            Long orderId = orderService.createNewOrder(orderReqDto, email);

            return ResponseEntity.ok("사용자: " + email + ", 주문 ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrderByUserEmail(@RequestParam(name = "email") String email) {
        List<OrderDto> orderDtos = orderService.getAllOrdersByUserEmail(email);

        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderItemDto> getOrderDetails(
            @PathVariable(name = "orderId") Long orderId,
            @RequestParam(name = "email") String email) {
        OrderItemDto orderItemDto = orderService.getOrderDetails(orderId, email);
        return ResponseEntity.ok(orderItemDto);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable(name = "orderId") Long orderId,
            @RequestParam(name = "email") String email
    ){
        try{
            orderService.cancelOrder(orderId, email);
            return ResponseEntity.ok("주문이 취소되었습니다.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 취소 실패: " + e.getMessage());
        }
    }
}
