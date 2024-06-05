package com.likelion12th.shop.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {

     @NotBlank(message = "이름은 필수 입력 값입니다.")
     private String name; // 이름

     @NotBlank(message = "이메일은 필수 입력 값입니다.")
     @Email(message = "이메일 형식으로 입력해주세요.")
     private String email; // 아이디(이메일)

     @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
     @Length(min = 8, max = 16, message ="비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
     private String password; // 비밀번호

     @NotBlank(message = "주소는 필수 입력 값입니다.")
     private String address; // 주소
}
