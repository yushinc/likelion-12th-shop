package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
public class Member extends Base{
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
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member=new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String pwd=passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(pwd);
        member.setRole(Role.USER);
        member.setAddress(memberFormDto.getAddress());


        return member;
    }
    public static Member createAdmin(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member admin=new Member();
        admin.setName(memberFormDto.getName());
        admin.setEmail(memberFormDto.getEmail());
        String pwd=passwordEncoder.encode(memberFormDto.getPassword());
        admin.setPassword(pwd);
        admin.setRole(Role.ADMIN);
        admin.setAddress(memberFormDto.getAddress());

        return admin;
    }


}
