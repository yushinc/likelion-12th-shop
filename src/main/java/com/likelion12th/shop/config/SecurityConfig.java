package com.likelion12th.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                );
        ;

        // 인가 설정 지정
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/members/login").anonymous()
                        .requestMatchers("/members/admin").hasRole("ADMIN")
                        .requestMatchers("/members/logout").authenticated()
                        .anyRequest().permitAll()
                );

        // 인증 실패 시 에러 만드는 것
        http
                .exceptionHandling(error -> error
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandle())
                );

        // CSRF 보호 기능을 활성화하고, CSRF 토큰을 쿠키에 저장하도록 구성
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );
        return http.build();

    }
}
