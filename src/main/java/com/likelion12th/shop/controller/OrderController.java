package com.likelion12th.shop.controller;

import com.likelion12th.shop.Dto.OrderDto;
import com.likelion12th.shop.Dto.OrderItemDto;
import com.likelion12th.shop.Dto.OrderReqDto;
import com.likelion12th.shop.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Controller
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    //주문하기
    @PostMapping("/new")
    // OrderReqDto와 email을 파라미터로 받아와서 새 주문을 생성하는 메소드
    // 클라이언트에서 POST 요청이 /api/orders/new로 오면 주문을 생성하고 그 결과를 반환합니다.
    public ResponseEntity<String> createNewOrder(@RequestBody OrderReqDto orderReqDto, @RequestParam ("email") String email ) {
        try {
            // dto와 email로 주문을 생성하는 메소드 호출
            Long orderId = orderService.createNewOrder(orderReqDto,email);
            // 성공적으로 생성될 경우 사용자의 이메일과 주문 id 반환
            return ResponseEntity.ok("사용자: "+email+", 주문ID: "+orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 실패 : "+e.getMessage());
        }
    }

    //주문 내역 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrderByUserEmail(@RequestParam("email") String email){
        //사용자 이메일을 파라미터로 받아와서 해당 사용자의 모든 주문을 조회
        List<OrderDto> orders = orderService.getAllOrdersByUserEmail(email);
        //조회된 주문목록을 클라이언트에 반환
        return ResponseEntity.ok(orders);
    }

    //주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderDetails(
            @PathVariable Long orderId,@RequestParam("email") String email) {
        // OrderId, Email로 특정 주문의 상세정보 조회
        List<OrderItemDto> orderItemDtos = orderService.getOrderDetails(orderId, email);
        return ResponseEntity.ok(orderItemDtos);
    }

    //주문 취소
    //@PathVariable Long orderId: URL 경로에서 orderId를 추출하여 메소드 매개변수로 전달
    //@RequestParam String email: 요청 URL의 쿼리 파라미터에서 email을 추출하여 메소드 매개변수로 전달
    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId,@RequestParam("email") String email) {
        try {
            //매개변수로 orderId, email를 전달하여 특정 주문을 취소
            orderService.cancelOrder(orderId,email);
            return ResponseEntity.ok("주문이 취소되었습니다.");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 취소 실패: "+e.getMessage());
        }
    }

}





