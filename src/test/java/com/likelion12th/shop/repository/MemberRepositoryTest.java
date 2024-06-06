package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void createMember() {
        Member member = new Member();
        member.setName("염정");
        member.setEmail("jung123@naver.com");
        member.setPassword("jung0430");
        member.setAddress("경기도 광명시");

        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());
    }
}