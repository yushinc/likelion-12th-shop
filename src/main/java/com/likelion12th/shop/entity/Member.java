package com.likelion12th.shop.entity;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Getter @Setter
@ToString // 이거 설정 안하면 실행했을 때 ?로 뜸
public class Member {
    @Id
    @Column(name="member_id")
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
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setAddress(memberFormDto.getAddress());

        return member;
    }

}

