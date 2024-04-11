package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 의존성 사용하기
@Transactional // 데이터 무결성을 위해 트랜잭션 내에서 실행하도록
@TestPropertySource(locations = "classpath:application.properties") // 선언해준 초반 sql 관련 코드 가져오기 위한 것
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void createMemberTest(){
        Member member = new Member();
        member.setEmail("yearim1226@naver.com");
        member.setName("yerim");
        member.setPassword("1234");
        member.setAddress("seoul");

        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());
    }

}