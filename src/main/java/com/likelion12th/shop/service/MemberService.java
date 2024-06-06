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
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        //이메일로 데이터베이스에서 유저 찾기
        Member member=memberRepository.findByEmail(email);
        if(member==null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(String.valueOf(member.getRole()))
                .build();
    }


    //회원가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        //데이터베이스에 회원 저장
        return memberRepository.save(member);
    }

    //중복회원 확인
    private void validateDuplicateMember(Member member){
        //이메일 사용하여 데이터베이스에 동일 회원 존재하는 지 확인
        Member member1 = memberRepository.findByEmail(member.getEmail());
        //회원이 존재한다면 에러 처리
        if(member1 !=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}