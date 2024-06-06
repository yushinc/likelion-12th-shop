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
@TestPropertySource(locations = "classpath:application.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("서영은");
        memberFormDto.setEmail("yeseo@swu.ac.kr");
        memberFormDto.setPassword("20020622");
        memberFormDto.setAddress("경기도");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember() {
        Member member = createMember();

        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getName(), savedMember.getName(), "이름이 다릅니다.");
        assertEquals(member.getEmail(), savedMember.getEmail(), "이메일이 다릅니다.");
        assertEquals(member.getAddress(), savedMember.getAddress(), "주소가 다릅니다.");
        assertEquals(member.getPassword(), savedMember.getPassword(), "비밀번호가 다릅니다.");

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