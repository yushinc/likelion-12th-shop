package com.likelion12th.shop.Controller;

import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.Service.MemberService;
import com.likelion12th.shop.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin";
    }
}
