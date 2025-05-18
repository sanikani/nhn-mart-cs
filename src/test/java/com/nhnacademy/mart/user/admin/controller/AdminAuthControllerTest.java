package com.nhnacademy.mart.user.admin.controller;

import com.nhnacademy.mart.user.admin.service.AdminService;
import com.nhnacademy.mart.user.auth.Role;
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

@WebMvcTest(controllers = AdminAuthController.class)
class AdminAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @Test
    @DisplayName("GET /cs/admin/auth/login - 세션 없으면 로그인 폼")
    void loginForm_noSession() throws Exception {
        mockMvc.perform(get("/cs/admin/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminLoginForm"));
    }

    @Test
    @DisplayName("GET /cs/admin/auth/login - 이미 로그인된 세션 있으면 /cs/admin 리다이렉트")
    void loginForm_alreadyLoggedIn() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("admin", "admin1");

        mockMvc.perform(get("/cs/admin/auth/login").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }

    @Test
    @DisplayName("GET /cs/admin/auth/login - 세션 admin이 빈 문자열이면 폼")
    void loginForm_emptyAdmin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("admin", "");

        mockMvc.perform(get("/cs/admin/auth/login").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("adminLoginForm"));
    }

    @Test
    @DisplayName("POST /cs/admin/auth/login - 성공 시 세션 생성, /cs/admin 리다이렉트")
    void doLogin_success() throws Exception {
        String adminId = "admin42";
        when(adminService.matches(adminId, "pw")).thenReturn(true);

        mockMvc.perform(post("/cs/admin/auth/login")
                        .param("id", adminId)
                        .param("password", "pw"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"))
                .andExpect(request().sessionAttribute("admin", adminId))
                .andExpect(request().sessionAttribute("role", Role.ADMIN));

        verify(adminService).matches(adminId, "pw");
    }

    @Test
    @DisplayName("POST /cs/admin/auth/login - 실패 시 폼 유지")
    void doLogin_failure() throws Exception {
        when(adminService.matches("x", "wrong")).thenReturn(false);

        mockMvc.perform(post("/cs/admin/auth/login")
                        .param("id", "x")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminLoginForm"));

        verify(adminService).matches("x", "wrong");
    }
}
