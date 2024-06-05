package com.likelion12th.shop.Controller;


import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 요청 시 회원가입 폼 페이지로 이동
    @GetMapping(value = "/new")
    public String memberForm(Model model, HttpServletRequest request) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // submit 버튼 누르면 호출되어 회원 생성
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            return "redirect:/";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
    }

    // 요청 시 로그인 폼 페이지로 이동
    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm";

    }

    // 로그인 실패한 경우
    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }

    // 요청 시 관리자 생성 폼 페이지로 이동
    @GetMapping(value = "/admincreate")
    public String showAdminCreateForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/adminForm";
    }

    @PostMapping(value = "/admincreate")
    public String adminCreateCount(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                                   Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/adminForm";
        }
        try {
            // 비밀번호를 해시화하지 않고 그대로 전달합니다.
            Member member = Member.createAdmin(memberFormDto, passwordEncoder);
            member.setRole(Role.ADMIN);
            memberService.saveAdmin(member);

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/adminForm";
        }
        return "redirect:/";
    }
}
