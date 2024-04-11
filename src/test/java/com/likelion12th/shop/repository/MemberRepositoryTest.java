package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("회원가입 및 상품 등록 테스트")
    public void createMember() {

        Member member = new Member();
        member.setName("김희원");
        member.setEmail("qtrachel@naver.com");
        member.setPassword("qwerty");
        member.setAddress("서울시");

        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());

        // 상품 정보 설정
        Product product = new Product();
        product.setName("아이폰 12");
        product.setPrice(Integer.valueOf("1200000")); // 가격은 예시입니다. 실제 값으로 수정하세요.
        product.setSize("M");
        product.setColor("그린");
        product.setCategory("전자제품");

        product.setMember(savedMember); // Member와 연관 설정

        Product savedProduct = productRepository.save(product); // 수정된 부분
        System.out.println(savedProduct.toString());
    }
}