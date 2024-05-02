package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
public class Member extends BaseTime {

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

    public static Member createMember(MemberFormDto memberFormDto) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setAddress(memberFormDto.getAddress());
        member.setPassword(memberFormDto.getPassword());
        member.setEmail(memberFormDto.getEmail());

        return member;
    }

}
