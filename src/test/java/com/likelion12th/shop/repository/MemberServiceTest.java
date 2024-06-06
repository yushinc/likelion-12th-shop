
package com.likelion12th.shop.repository;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest // 추가해야 밑에 의존성주입 문제없이 됨.
@TestPropertySource(locations="classpath:application.properties")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto(); //객체생성 후
        memberFormDto.setName("jung");
        memberFormDto.setEmail("jung@gmail.com");
        memberFormDto.setAddress("경기도");
        memberFormDto.setPassword("12340987");

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return member;
    }


    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember() {
        //createMember()로 회원 객체 생성. 위에 만들어놨으니까 new로 또 생성하는거 아님.
        Member member = this.createMember();
        //데이터베이스에 저장
        Member member1 = memberService.saveMember(member);
        //assertEquals로 생성한 객체와 저장한 객체의 데이터가 일치하는지 개별비교
        assertEquals(member.getName(), member1.getName());
        assertEquals(member.getEmail(), member1.getEmail());
        assertEquals(member.getAddress(), member1.getAddress());
        assertEquals(member.getRole(), member1.getRole());
        assertEquals(member.getPassword(), member1.getPassword());

    }


    @Test
    @DisplayName("중복 회원가입 테스트")
    public void setDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

       Throwable e = assertThrows(IllegalStateException.class, () -> {
           memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}








