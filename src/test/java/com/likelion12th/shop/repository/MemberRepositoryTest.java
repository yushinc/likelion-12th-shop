package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 테스트")
    public void CreateMemberTest(){
        Member member=new Member();
        member.setName("이민경");
        member.setEmail("test@naver.com");
        member.setPassword("test");
        member.setAddress("서울시");


        Member savedMember=memberRepository.save(member);
        System.out.println(savedMember.toString());

    }
}