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

        //1.멤버 객체 생성
        Member member = new Member();

        //2. 멤버 객체에 이름, 아이디(이메일),비밀번호, 주소 직접 세팅
        member.setName("김세은");
        member.setEmail("rlatpdms0104@naver.com");
        member.setPassword("qwerty1234");
        member.setAddress("세종시");

        Member savedMember=memberRepository.save(member);
        //memberRepository의 save메소드를 사용하여 해당 객체 저장
        //-해당 객체를 저장할 새로운 멤버 객체 생성 필요, save(member)로 저장

        System.out.println(savedMember.toString());
        //출력 + memeber엔티티에 @ToString 어노테이션 추가 후 member.toString()을 출력
    }

}