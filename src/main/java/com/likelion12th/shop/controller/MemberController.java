package com.likelion12th.shop.controller;


import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // submit 버튼 누르면 호출되어 회원 생성
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("error발생");
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
    @GetMapping("/login")
    public String loginMember(){return "/member/memberLoginForm";}

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";

    }
    @GetMapping(value = "/adminnew")
    public String adminMemberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // submit 버튼 누르면 호출되어 회원 생성
    @PostMapping(value = "/adminnew")
    public String adminMemberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("error발생");
            return "member/memberForm";
        }
        try {
            Member admin = Member.createAdmin(memberFormDto, passwordEncoder);
            admin.setRole(Role.ADMIN);
            memberService.saveAdmin(admin);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }



}