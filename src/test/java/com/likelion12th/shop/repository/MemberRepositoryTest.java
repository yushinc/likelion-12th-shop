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
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void createMemberTest(){
        Member member = new Member();
        member.setName("김세은");
        member.setEmail("rlatpdms0104@naver.com");
        member.setPassword("qwerty1234");
        member.setAddress("세종시");

        Member savedMember=memberRepository.save(member);
        System.out.println(savedMember.toString());
    }

}