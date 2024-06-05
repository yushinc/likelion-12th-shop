package com.likelion12th.shop.Service;

import com.likelion12th.shop.dto.CartDetailDto;
import com.likelion12th.shop.dto.CartItemDto;
import com.likelion12th.shop.dto.CartOrderDto;
import com.likelion12th.shop.dto.OrderReqDto;
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

@Service @Transactional @RequiredArgsConstructor //final이 붙은 필드를 자동으로 의존성 주입
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService; //final을 안붙였더니 this.orderservice 오류 발생

    public Long addCartItems(String email, List<CartItemDto> cartItemDtos) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            Member newmember = new Member();
            newmember.setEmail(email);
            member = memberRepository.save(newmember);
        }
        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            Cart newCart = cart.createCart(member);
            cart=cartRepository.save(newCart);
        }
        Long saveItemId = null;
        for (CartItemDto cartItemDto : cartItemDtos) {
            Item item = itemRepository.findById(cartItemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("아이템 ID가 없음"));
            CartItem saveCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
            if (saveCartItem != null) {
                saveCartItem.addCount(cartItemDto.getCount());
                saveItemId = saveCartItem.getId();
            } else {
                CartItem newItem = saveCartItem.createCartItem(cart, item, cartItemDto.getCount());
                cartItemRepository.save(newItem);
                saveItemId = newItem.getId();
            }

        }

        return saveItemId;
    }
    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList=new ArrayList<>();

        Member member=memberRepository.findByEmail(email);
        Cart cart =cartRepository.findByMember(member);
        if (cart==null){
            return cartDetailDtoList;
        }
        cartDetailDtoList=cartItemRepository.findCartOrderList(cart.getId());
        return cartDetailDtoList;
    }
    public void deleteCartItem(String email, Long cartItemId){
        CartItem cartItem=cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        if(!email.equals(cartItem.getCart().getMember().getEmail())){
            throw new IllegalArgumentException();
        }
        cartItemRepository.delete(cartItem);
    }
    public void updateCartItemCount(String email, CartDetailDto cartDetailDto){
        CartItem cartItem=cartItemRepository.findById(cartDetailDto.getCartItemId())
                .orElseThrow(EntityNotFoundException::new);

        if(!cartItem.getCart().getMember().getEmail().equals(email)){
            throw new IllegalArgumentException();
        }
        cartItem.updateCount(cartDetailDto.getCount());
    }
    public Long orderCartItem(String email, List<CartOrderDto> cartOrderDtoList){//장바구니 주문+ 주문 시 장바구니 초기화
        Member member=memberRepository.findByEmail(email);
        if(member == null){
            throw new EntityNotFoundException("사용자가 존재하지 않음 : "+ email);
        }
        Cart cart=cartRepository.findByMember(member);
        if(cart==null){
            throw new EntityNotFoundException("사용자의 장바구니가 존재하지 않음 : "+email);
        }
        List<OrderReqDto> orderDtoList=new ArrayList<>();
        for(CartOrderDto cartOrderDto:cartOrderDtoList){
            CartItem cartItem=cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);  //장바구니 아이템 조회
            OrderReqDto orderReqDto=new OrderReqDto();
            orderReqDto.setItemId(cartItem.getItem().getId()); //cartitem의 아이디와 item 자체의 아이디는 다르다!!
            orderReqDto.setCount(cartItem.getCount());
            orderDtoList.add(orderReqDto);
        }
        Long orderId=orderService.orders(orderDtoList,email);
        for(CartOrderDto cartOrderDto:cartOrderDtoList){//장바구니 초기화
            CartItem cartItem=cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
}
