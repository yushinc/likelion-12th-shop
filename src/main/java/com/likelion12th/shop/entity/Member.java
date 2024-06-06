package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Member extends Base {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setAddress(memberFormDto.getAddress());
        String pwd = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(pwd);
        member.setRole(Role.USER);
        member.setEmail(memberFormDto.getEmail());

        return member;
    }

}
