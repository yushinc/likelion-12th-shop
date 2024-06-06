package com.likelion12th.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("admin 테스트")
    @WithMockUser(username="admin",roles="ADMIN")
    public void adminFormtest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/"))
                // 요청과 응답 메시지를 확인 할 수 있도록 콘솔창에 출력
                .andDo(print())
                // 응답 상태 코드가 정상인지 확인
                .andExpect(status().isOk());
    }

}