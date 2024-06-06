package com.likelion12th.shop.controller;

import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 요청 시 회원가입 폼 페이지로 이동
    @GetMapping(value = "/new")
    public String memberForm(Model model, HttpServletRequest request) {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);

        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // submit 버튼 누르면 호출되어 회원 생성
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);

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

        return "redirect:/";
    }

    // 요청 시 로그인 폼 페이지로 이동
    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    // 로그인 실패한 경우
    @GetMapping("/login/error")
    public String loginError(Model model, HttpServletRequest request){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);

        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    // 요청 시 관리자 페이지로 이동
    @GetMapping("/admin")
    public String admin() {
        return "member/admin";
    }

    // 요청 시 관리자 페이지로 이동
    @GetMapping("/admin/error")
    public String adminError(Model model, HttpServletRequest request) {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);

        model.addAttribute("adminErrorMsg", "관리자 권한이 없습니다.");
        return "member/memberLoginForm";
    }
}

