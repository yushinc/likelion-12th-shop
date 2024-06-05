package com.likelion12th.shop.Service;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.likelion12th.shop.entity.QMember.member;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일로 데이터베이스에서 유저 찾기
        Member member = memberRepository.findByEmail(email);

        // 멤버가 존재하지 않는다면 UsernameNotFoundException 처리
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        // UserDetails를 구현하고 있는 User 객체 반환하기
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword()) // 패스워드 추가
                .roles("USER") // 권한 추가 (ROLE_USER)
                .build();
    }

    // 회원 가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터 베이스에 회원 저장
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) { // 회원이 존재한다면 에러 처리
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // admin 회원 가입
    public Member saveAdmin(Member member) {
        validateDuplicateMember(member);
        member.setRole(Role.ADMIN);
        return memberRepository.save(member);
    }
}



