package com.likelion12th.shop.Service;

import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.constraints.Email;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;


    //회원가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        //데이터베이스에 회원 저장
        return memberRepository.save(member);
    }

    //중복 회원 확인
    private void validateDuplicateMember(Member member) {
        // 이메일을 사용하여 데이터베이스에서 동일한 회원을 조회
        Member existMember = memberRepository.findByEmail(member.getEmail());

        // 회원이 존재한다면 이미 가입된 회원이므로 예외를 발생시킴
        if (existMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일로 데이터베이스에서 유저 찾기
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }

        }