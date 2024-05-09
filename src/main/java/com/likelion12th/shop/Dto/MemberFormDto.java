package com.likelion12th.shop.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFormDto {
     private String shop12;
     private String name;// 이름
     private String email;// 아이디(이메일)
     private String password;// 비밀번호
     private String address;// 주소
}
