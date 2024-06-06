package com.likelion12th.shop.controller;

import com.likelion12th.shop.dto.OrderDto;
import com.likelion12th.shop.dto.OrderItemDto;
import com.likelion12th.shop.dto.OrderReqDto;
import com.likelion12th.shop.entity.Order;
import com.likelion12th.shop.service.OrderService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;


    // orderReqDto: body 안에 담아 보냄
    @PostMapping("/new")
    public ResponseEntity<String> createNewOrder(@RequestBody OrderReqDto orderReqDto,
                                                 @RequestParam String email){
        try {
            Long orderId = orderService.createNewOrder(orderReqDto, email);

            return ResponseEntity.ok("사용자: " + email + ", 주문 ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 실패: " + e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrderByUserEmail(@RequestParam String email) {
        List<OrderDto> orders = orderService.getAllOrdersByUserEmail(email);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderDetails(@PathVariable Long orderId,
                                                        @RequestParam("email") String email) {
//        OrderItemDto orderItemDto = orderService.getOrderDetails(orderId, email);
//
//        return ResponseEntity.ok(orderItemDto);
        List<OrderItemDto> orderItemDtos = orderService.getOrderDetails(orderId, email);
        return ResponseEntity.ok(orderItemDtos);
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId,
                                              @RequestParam String email) {
        try {
            orderService.cancelOrder(orderId, email);
            return ResponseEntity.ok("주문이 취소되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 취소 실패: " + e.getMessage());
        }

    }
}
