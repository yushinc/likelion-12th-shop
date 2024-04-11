package com.likelion12th.shop.entity;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Getter @Setter @ToString
public class Member {

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

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;
public static Member createMember(MemberFormDto memberFormDto) {
    Member member = new Member();
    member.setName(memberFormDto.getName()); // 이름
    member.setEmail(memberFormDto.getEmail());// 이메일
    member.setPassword(memberFormDto.getPassword());// 비밀번호
    member.setAddress(memberFormDto.getAddress());// 주소

    return member;
    }
}
