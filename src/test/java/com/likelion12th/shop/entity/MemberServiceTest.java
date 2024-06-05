package com.likelion12th.shop.entity;

import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("seohee");
        memberFormDto.setEmail("seohee@naver.com");
        memberFormDto.setPassword("12345678");
        memberFormDto.setAddress("서울");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return member;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember() {
        Member member = this.createMember();
        Member member1 = memberService.saveMember(member);
        assertEquals(member.getEmail(), member1.getEmail());
        assertEquals(member.getName(), member1.getName());
        assertEquals(member.getRole(), member1.getRole());
        assertEquals(member.getPassword(), member1.getPassword());
        assertEquals(member.getAddress(), member1.getAddress());
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
