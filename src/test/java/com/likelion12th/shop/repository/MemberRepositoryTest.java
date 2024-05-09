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
    @DisplayName("회원가입 테스트")
    public void createMember() {

        Member member = new Member();
        member.setName("김희원");
        member.setEmail("qtrachel@naver.com");
        member.setPassword("qwerty");
        member.setAddress("서울시");

        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());

    }
}