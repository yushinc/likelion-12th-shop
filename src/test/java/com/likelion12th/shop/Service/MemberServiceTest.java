package com.likelion12th.shop.Service;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setName("이름");
        memberFormDto.setPassword("비밀번호");
        memberFormDto.setEmail("이메일");
        memberFormDto.setAddress("주소");

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return member;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember() {

        Member member = createMember(); // createMember()로 회원 객체 생성
        Member savedMember = memberService.saveMember(member); // 생성한 회원 객체를 저장

        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getAddress(), savedMember.getAddress());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void setDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1); // member1 저장

        Throwable e = assertThrows(IllegalStateException.class, ()-> {
            memberService.saveMember(member2); // member2 저장
        });

        assertEquals("이미 가입된 회원입니다.",e.getMessage());
    }
}
