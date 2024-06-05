package com.likelion12th.shop.entity;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name="member")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString
public class Member extends BaseTime {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName()); // 이름
        member.setEmail(memberFormDto.getEmail());// 이메일
        String pwd = passwordEncoder.encode(memberFormDto.getPassword());// 비밀번호
        member.setPassword(pwd);
        member.setRole(Role.USER); // 역할 설정
        member.setAddress(memberFormDto.getAddress()); // 주소
        return member;
    }

    public static Member createAdmin(MemberFormDto memberFormDto,
                                     PasswordEncoder passwordEncoder) {
        Member admin = new Member();
        admin.setName(memberFormDto.getName());
        admin.setEmail(memberFormDto.getEmail());
        String pwd = passwordEncoder.encode(memberFormDto.getPassword());// 비밀번호
        admin.setPassword(pwd);
        admin.setRole(Role.ADMIN);
        admin.setAddress(memberFormDto.getAddress());

        return admin;
    }

    public void setMemberName(String 테스트_회원) {
    }
}
