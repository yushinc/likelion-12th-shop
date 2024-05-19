package com.likelion12th.shop.dto;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class MemberFormDto {
    //DTO : 계층간 데이터 교환을 위해 사용하는 객체

    private String name; //이름
    private String email; //아이디(이메일)
    private String password; //비밀번호
    private String address; //주소


}

