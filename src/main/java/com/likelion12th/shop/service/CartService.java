package com.likelion12th.shop.service;

import com.likelion12th.shop.dto.CartDetailDto;
import com.likelion12th.shop.dto.CartItemDto;
import com.likelion12th.shop.dto.CartOrderDto;
import com.likelion12th.shop.dto.OrderReqDto;
import com.likelion12th.shop.entity.Cart;
import com.likelion12th.shop.entity.CartItem;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.*;
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
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    // 장바구니에 상품 여러 개 담기(생성)
    public Long addCartItems(String email, List<CartItemDto> cartItemDtos) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            Member newMember = new Member();
            newMember.setEmail(email);
            member = memberRepository.save(newMember);
        }

        // 회원의 장바구니를 조회
        Cart cart = cartRepository.findByMember(member);
        if(cart == null) {
            Cart newCart = Cart.createCart(member);
            cart = cartRepository.save(newCart);
        }

        // 장바구니에 상품 담기
        Long savedItemId = null;
        for(CartItemDto cartItemDto:cartItemDtos) {
            Item item = itemRepository.findById(cartItemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("아이템ID가 없음"));

            // 장바구니에 이미 있는 상품인지 확인
            CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), cartItemDto.getItemId());

            if (savedCartItem != null) {
                savedCartItem.updateCount(cartItemDto.getCount());
                savedItemId = savedCartItem.getId();
            } else {
                CartItem savedItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
                cartItemRepository.save(savedItem);
                savedItemId = savedItem.getId();
            }
        }

        return savedItemId;
    }

    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMember(member);
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartOrderList(cart.getId());
        return cartDetailDtoList;
    }

    public void deleteCartItem(String email, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        if(!email.equals(cartItem.getCart().getMember().getEmail())) {
            throw new IllegalArgumentException();
        }

        cartItemRepository.delete(cartItem);
    }

    public void updateCartItemCount(String email, CartDetailDto cartDetailDto) {
        CartItem cartItem = cartItemRepository.findById(cartDetailDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

        if(!cartItem.getCart().getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException();
        }

        cartItem.updateCount(cartDetailDto.getCount());
    }

    // 장바구니 주문, 장바구니 초기화
    public Long orderCartItem(String email, List<CartOrderDto> cartOrderDtoList) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        if(member == null) {        // 해당 이메일로 가입된 멤버 없으며 오류 발생
            throw new EntityNotFoundException("사용자가 존재하지 않음: " + email);
        }

        // 해당 회원의 장바구니 조회
        Cart cart = cartRepository.findByMember(member);
        if(cart == null) {
            throw new EntityNotFoundException("사용자의 장바구니가 존재하지 않음: " + email);
        }

        List<OrderReqDto> orderReqDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderReqDto orderReqDto = new OrderReqDto();
            orderReqDto.setItemId(cartItem.getId());
            orderReqDto.setCount(cartItem.getCount());
            orderReqDtoList.add(orderReqDto);
        }

        Long orderId = orderService.orders(orderReqDtoList, email);
        for(CartOrderDto cartOrderDto : cartOrderDtoList ){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }


}
