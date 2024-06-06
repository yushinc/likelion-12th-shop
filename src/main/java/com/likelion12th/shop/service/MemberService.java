package com.likelion12th.shop.service;

import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member member1 = memberRepository.findByEmail(member.getEmail());

        if (member1 != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            System.out.println("Not Found Member" );
            System.out.println("memberEmail: " + email);
            throw new UsernameNotFoundException(email);
        }

        System.out.println("Found member: " + member);

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
//                .roles(String.valueOf(member.getRole()))
                .roles(member.getRole().toString())
                .build();
    }


}
