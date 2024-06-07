package com.likelion12th.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 에러 핸들링 코드
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인증 시 인증되지 않은 사용자가 Ajax 요청(if문 안의 조건)
    // Ajax는 javaScript를 사용한 비동기 통신, 클라이언트와 서버 간에 XML, JSON 데이터를 주고 받는 기술
    // 페이지를 새로 고치지 않아도 뷰를 갱신 가능
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException, ServletException {
        if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }else{
            response.sendRedirect("/members/login");
        }
    }

}
