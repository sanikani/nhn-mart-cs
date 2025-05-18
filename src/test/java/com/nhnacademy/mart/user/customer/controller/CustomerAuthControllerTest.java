package com.nhnacademy.mart.user.customer.controller;

import com.nhnacademy.mart.user.auth.Role;
import com.nhnacademy.mart.user.customer.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerAuthController.class)
class CustomerAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    @DisplayName("GET /cs/auth/login - 세션 없으면 로그인 폼 뷰 반환")
    void loginForm_noSession() throws Exception {
        mockMvc.perform(get("/cs/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    @DisplayName("GET /cs/auth/login - 세션에 id 있으면 /cs로 리다이렉트")
    void loginForm_alreadyLoggedIn() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", "user1");

        mockMvc.perform(get("/cs/auth/login").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));
    }

    @Test
    @DisplayName("GET /cs/auth/login - 세션에 빈 id이면 로그인 폼")
    void loginForm_emptyId() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", "");

        mockMvc.perform(get("/cs/auth/login").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    @DisplayName("POST /cs/auth/login - 인증 성공 시 세션 생성하고 /cs로 리다이렉트")
    void doLogin_success() throws Exception {
        String userId = "user42";
        String pwd = "pass";
        when(customerService.matches(userId, pwd)).thenReturn(true);

        mockMvc.perform(post("/cs/auth/login")
                        .param("id", userId)
                        .param("password", pwd))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"))
                .andExpect(request().sessionAttribute("id",userId))
                .andExpect(request().sessionAttribute("role", Role.CUSTOMER));

        verify(customerService).matches(userId, pwd);
    }

    @Test
    @DisplayName("POST /cs/auth/login - 인증 실패 시 로그인 폼 뷰 유지")
    void doLogin_failure() throws Exception {
        when(customerService.matches("userX", "wrong")).thenReturn(false);

        mockMvc.perform(post("/cs/auth/login")
                        .param("id", "userX")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));

        verify(customerService).matches("userX", "wrong");
    }

    @Test
    @DisplayName("GET /cs/auth/logout - 세션 무효화 후 index 뷰")
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", "user1");

        mockMvc.perform(get("/cs/auth/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request().sessionAttributeDoesNotExist("id"));
    }
}