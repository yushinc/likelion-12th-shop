package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends Base {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique=true)
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    //private String createdBy;
    //private String modifiedBy;


//엔티티 생성 메소드
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member= new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String pwd= passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(pwd);
        member.setRole(Role.ADMIN);
        member.setAddress(memberFormDto.getAddress());

        return member;
    }

    public void setMemberName(String 테스트_회원) {
    }
}

