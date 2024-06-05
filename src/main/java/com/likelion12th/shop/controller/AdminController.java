package com.likelion12th.shop.controller;

import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/page")
    public String adminPage() {
        return "member/admin";
    }



}
