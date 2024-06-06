package com.likelion12th.shop.service;

import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        //MemberFormDto 객체 생성
        MemberFormDto memberFormDto = new MemberFormDto();
        //MemberFormDto 필드 직접 채우기
        memberFormDto.setName("김세은");
        memberFormDto.setEmail("a@naver.com");
        memberFormDto.setPassword("123456");
        memberFormDto.setAddress("서울시");
        //Member 엔티티의 createMember() 메소드 사용하여 객체 생성 후 반환
        return Member.createMember(memberFormDto,passwordEncoder);

    }
    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember(){
        //createMember()로 회원 객체 생성
        Member member = this.createMember();
        //데이터베이스에 저장
        Member savedMember = memberService.saveMember(member);
        //assertEquals로 생성한 객체랑 저장한 객체의 데이터 일치하는 지 개별 비교
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getId(), savedMember.getId());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getRole(), savedMember.getRole());
        assertEquals(member.getAddress(), savedMember.getAddress());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void setDuplicateMemberTest(){
        Member member1=createMember();
        Member member2=createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class,()->{
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.",e.getMessage());
    }

}