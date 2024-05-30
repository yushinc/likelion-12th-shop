package com.likelion12th.shop.entity;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="member")
@EntityListeners(AuditingEntityListener.class) //선언 안했었음. 주의
@Getter  @Setter
@ToString
public class Member extends BaseTime {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)  //중복방지
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

    public void setMemberName(String 테스트_회원) {
    }
}