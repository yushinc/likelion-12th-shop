package com.likelion12th.shop.Service;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.awt.*;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor //의존성 주입 중 생성자 주입을 코드 없이 자동
public class MemberService implements UserDetailsService{
    private final MemberRepository memberRepository;

    //회원가입
    public Member saveAdmin(Member member){
        member.setRole(Role.ADMIN);
        validateDuplicateMember(member);
        return memberRepository.save(member); //save자체로 member를 반환
    }
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member); //save자체로 member를 반환
    }



    private void validateDuplicateMember(Member member) {
        Member valiMember= memberRepository.findByEmail(member.getEmail());
        if(valiMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다. ");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
