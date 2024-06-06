package com.likelion12th.shop.controller;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 요청 시 회원가입 폼 페이지로 이동
    @GetMapping(value = "/new")
    public String memberForm(Model model) {

        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // submit 버튼 누르면 호출되어 회원 생성
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/members/login";

    }

    //요청 시 로그인 폼 페이지로 이동
    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm"; // 로그인 폼 페이지의 뷰 이름을 반환합니다.
    }

    //로그인 실패한 경우
    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요."); // 에러 메시지를 모델에 추가합니다.
        return "member/memberLoginForm"; // 실패 시 다시 로그인 폼 페이지로 이동합니다.
    }

    // 관리자 회원가입
    @PostMapping(value = "/admin")
    public String AdminMember(@Valid MemberFormDto memberFormDto,
                                 BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "member/adminForm";
        }

        try {
            Member member = Member.CreateAdminMember(memberFormDto, passwordEncoder);
            member.setRole(Role.ADMIN);
            memberService.saveMember(member);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/adminForm";
        }

        return "redirect:/";
    }


}