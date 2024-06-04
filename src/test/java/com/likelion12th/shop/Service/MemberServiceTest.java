package com.likelion12th.shop.Service;

import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
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
@TestPropertySource(locations="classpath:application.properties")
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto=new MemberFormDto();
        memberFormDto.setName("user1");
        memberFormDto.setEmail("123@naver.com");
        memberFormDto.setAddress("노원구");
        memberFormDto.setPassword("asdf1234");
        Member member=Member.createAdmin(memberFormDto,passwordEncoder);
        return member;
    }
    @Test
    @DisplayName("회원가입 테스트")
    public void saveMember(){
        Member member=createMember();
        Member savemember=memberService.saveMember(member);
        assertEquals(member.getEmail(),savemember.getEmail());
        assertEquals(member.getName(),savemember.getName());
        assertEquals(member.getAddress(),savemember.getAddress());
        assertEquals(member.getPassword(),savemember.getPassword());
        //생성한 객체와 저장한 객체의 필드끼리 비교
        System.out.println(savemember.getRole().toString());
     }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void setDuplicateMemberTest(){
        Member member1=createMember();
        Member member2=createMember();
        memberService.saveMember(member1);

        Throwable e=assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });
        assertEquals("이미 가입된 회원입니다. ",e.getMessage());
        }
    }

