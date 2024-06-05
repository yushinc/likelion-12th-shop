package com.likelion12th.shop.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class ProductRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품 등록 테스트")
    public void CreateProductTest(){
        Member member = new Member();

        Member savedMember = memberRepository.save(member);

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