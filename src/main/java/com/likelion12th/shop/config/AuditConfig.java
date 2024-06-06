package com.likelion12th.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration // @Bean 어노테이션을 사용하여 빈을 정의가능
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwarelmpl();
    }
}
