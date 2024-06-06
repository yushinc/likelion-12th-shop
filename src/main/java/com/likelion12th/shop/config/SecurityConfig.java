package com.likelion12th.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
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
        http

                .formLogin(form -> form
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );


        //보안검사
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/members/login").anonymous()
                        .requestMatchers("/members/logout").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자페이지 접근제한
                        .anyRequest().permitAll()

                );


        //인증 실패시 대처방법 커스텀
        http
                .exceptionHandling(error -> error
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }

}








