package com.likelion12th.shop.Service;

import com.likelion12th.shop.Dto.CartDetailDto;
import com.likelion12th.shop.Dto.CartItemDto;
import com.likelion12th.shop.Dto.CartOrderDto;
import com.likelion12th.shop.Dto.OrderReqDto;
import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.CartItem;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.CartItemRepository;
import com.likelion12th.shop.repository.CartRepository;
import com.likelion12th.shop.repository.ItemRepository;
import com.likelion12th.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService;

    // 장바구니에 상품 여러 개 담기(생성하기)
    public Long addCartItems(String email, List<CartItemDto> cartItemDtos) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);

        // 회원이 존재하지 않을 경우 email로 새로운 회원 생성
        if (member == null) {
            Member newMember = new Member();
            newMember.setEmail(email);
            member = memberRepository.save(newMember);
        }

        // 회원의 장바구니 조회
        Cart cart = cartRepository.findByMember(member); // Member로 회원의 장바구니 조회
        // orderId로 주문 조회

        // 장바구니가 존재하지 않을 경우 새롭게 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 장바구니에 담을 상품 아이디
        Long saveItemId = null;
        for (CartItemDto cartItemDto : cartItemDtos) {
            // 상품 아이디로 상품 조회
            Item item = itemRepository.findById(cartItemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("아이템 ID가 없음"));

            // 장바구니 ID와 상품 ID로 장바구니에 이미 있는 상품인지 확인
            CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

            if (savedCartItem != null) {
                // 이미 장바구니에 있는 상품인 경우 수량 추가
                savedCartItem.addCount(cartItemDto.getCount());
                saveItemId = savedCartItem.getId();
            } else {
                // 장바구니에 없는 상품인 경우 새로운 cartItem 생성 (힌트: 엔티티에 작성된 메서드 사용!)
                CartItem newCartItem = CartItem.createCartItem(cart,item,cartItemDto.getCount());
                CartItem savedItem = cartItemRepository.save(newCartItem);
                saveItemId = savedItem.getId();
            }

        }

        return saveItemId;
    }

    // 장바구니 조회하기
    public List<CartDetailDto> getCartList(String email) {
        // 장바구니 상품 리스트 생성하기
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email); // 회원 정보 조회
        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartOrderList(cart.getId());
        return cartDetailDtoList;
    }

    // 장바구니에서 상품 삭제하기
    public void deleteCartItem(String email, Long cartItemId) {
        // 장바구니 아이템 id로 장바구니 아이템 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 장바구니 아이템이 해당 사용자의 장바구니에 속해 있는지 이메일 비교를 통해 확인
        if (!cartItem.getCart().getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException();
        }

        // 상품 삭제
        cartItemRepository.delete(cartItem);
    }

    // 장바구니에서 상품 수량 변경하기
    public void updateCartItemCount(String email, CartDetailDto cartDetailDto) {

        // 장바구니 아이템 id로 장바구니 아이템 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartDetailDto.getCartItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 장바구니 아이템이 해당 사용자의 장바구니에 속해 있는지 확인
        if (!cartItem.getCart().getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException();
        }

        // 수량 업데이트
        // 힌트 : 상품 수량 업데이트 메서드 불러오기
        cartItem.updateCount(cartDetailDto.getCount());
    }
    // 장바구니 주문 + 주문 시 장바구니 초기화
    public Long orderCartItem(String email, List<CartOrderDto> cartOrderDtoList) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("사용자가 존재하지 않음 : " + email);
        }
        // 회원의 장바구니 조회
        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            throw new EntityNotFoundException("사용자의 장바구니가 존재하지 않음 : " + email);
        }
        // 주문할 상품을 담을 리스트 생성
        List<OrderReqDto> orderDtoList = new ArrayList<>();

        // 전달 받은 cartOrderDto 리스트를 순회
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 장바구니 아이템 ID로 장바구니 아이템 조회
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            // 주문 요청 DTO 생성 및 설정
            OrderReqDto orderReqDto = new OrderReqDto();
            // 아이템 ID 설정
            orderReqDto.setItemId(cartItem.getItem().getId());  // 장바구니에 담긴 상품의 아이디 가져오기
            // 아이템 수량 설정
            orderReqDto.setCount(cartItem.getCount()); // 장바구니에 담긴 상품의 구매 수량 가져오기
            // 주문 요청 DTO 리스트에 추가
            orderDtoList.add(orderReqDto);  // 주문할 상품을 담는 리스트에 추가하기
        }

        // orderService 호출하여 주문 ID 반환
        Long orderId = orderService.orders(orderDtoList, email);
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 장바구니 아이템 ID로 장바구니 아이템 조회
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            // 장바구니 초기화(아이템 삭제)
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
}
































