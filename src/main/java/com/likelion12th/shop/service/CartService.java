package com.likelion12th.shop.service;

import com.likelion12th.shop.dto.*;
import com.likelion12th.shop.entity.*;
import com.likelion12th.shop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.likelion12th.shop.entity.QCart.cart;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService;

    public Long addCartItems(String email, List<CartItemDto> cartItemDtos) {
        //이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        // 회원이 존재하지 않을 경우 email로 새로운 회원 생성
        if (member == null) {
            Member newMember = new Member();
            newMember.setEmail(email);
            member = memberRepository.save(newMember);
        }
        //회원의 장바구니 조회-member로 회원의 장바구니 조회
        Cart cart = cartRepository.findByMember(member);
        //장바구니가 존재하지 않을 경우 새롭게 생성
        if (cart == null) {
            //회원의 정보로 cart 객체 생성(hint:엔티티에 작성된 메서드 사용)
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Long savedItemId = null;
        for (CartItemDto cartItemDto : cartItemDtos) {
            Item item = itemRepository.findById(cartItemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("아이템 ID가 없음"));

            //장바구니 ID와 상품 ID로 장바구니에 이미 있는 상품인지 확인
            CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

            if (savedCartItem != null) {
                //이미 장바구니에 있는 상품인 경우 수량 추가(hint:엔티티에 작성된 메서드 사용)
                savedCartItem.addCount(cartItemDto.getCount());
                savedItemId = savedCartItem.getId();
            } else {
                //장바구니에 없는 상품인 경우 새로운 cartItem 생성
                CartItem newCartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());

                CartItem savedItem = cartItemRepository.save(newCartItem);
                savedItemId = savedItem.getId();

            }
        }
        return savedItemId;


    }

    public List<CartDetailDto> getCartList(String email) {

        //장바구니 상품 리스트 생성
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            return cartDetailDtoList;
        }
        //장바구니 id로 cart 조회 후 아이템 리스트 가져오기
        cartDetailDtoList = cartItemRepository.findCartOrderList(cart.getId());
        return cartDetailDtoList;
    }

    public void deleteCartItem(String email, Long cartItemId) {
        //장바구니 아이템 id로 장바구니 아이템 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        //장바구니 아이템이 해당 사용자의 장바구니에 속해 있는지 학인(이메일 비교)
        if (!cartItem.getCart().getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException();
        }
        cartItemRepository.delete(cartItem);
    }

    //장바구니에서 상품 수량 변경하기
    public void updateCartItemCount(String email, CartDetailDto cartDetailDto) {
        //장바구니 아이템 id로 장바구니 아이템 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartDetailDto.getCartItemId())
                .orElseThrow(EntityNotFoundException::new);

        //장바구니 아이템이 해당 사용자의 장바구니에 속해 있는지 확인
        if (!cartItem.getCart().getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException();
        }

        //수량 업데이트
        //hint : 상품 수량 업데이트 메서드 불러오기
        cartItem.updateCount(cartDetailDto.getCount());
    }

    //장바구니 주문+주문 시 장바구니 초기화
    public Long orderCartItem(String email, List<CartOrderDto> cartOrderDtoList) {
        //이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("사용자가 존재하지 않음 : " + email);
        }

        //회원의 장바구니 조회
        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            throw new EntityNotFoundException("사용자의 장바구니가 존재하지 않음 : " + email);
        }

        //주문할 상품을 담을 리스트 생성
        List<OrderReqDto> orderDtoList = new ArrayList<>();

        //전달받은 cartOrderDto 리스트를 순회
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            //장바구니 아이템 ID로 장바구니 아이템 조회
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            //주문 요청 DTO 생성 및 설정
            OrderReqDto orderReqDto = new OrderReqDto();
            //아이템 ID 설정 / 장바구니에 담긴 상품의 ID 가져오기
            orderReqDto.setItemId(cartItem.getItem().getId());
            //아이템 수량 설정 / 장바구니에 담긴 상품의 구매 수량 가져오기
            orderReqDto.setCount(cartItem.getCount());
            //주문 요청 DTO 리스트에 추가 / 주문할 상품을 담는 리스트에 추가하기
            orderDtoList.add(orderReqDto);
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


