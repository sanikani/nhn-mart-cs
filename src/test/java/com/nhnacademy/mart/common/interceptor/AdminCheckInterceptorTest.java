package com.nhnacademy.mart.common.interceptor;


import com.nhnacademy.mart.user.auth.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminCheckInterceptorTest {

    @InjectMocks
    private AdminCheckInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private HttpSession session;

    @Test
    @DisplayName("세션이 없으면 관리자 로그인 페이지로 리다이렉트 후 false 반환")
    void preHandle_noSession_redirectsAndReturnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/cs/admin/protected");

        boolean result = interceptor.preHandle(request, response, handler);

        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/admin/auth/login");
        verify(request).getSession(false);
        verify(request).getRequestURI();
    }

    @Test
    @DisplayName("세션은 있지만 admin 속성이 없으면 리다이렉트 후 false 반환")
    void preHandle_noAdminAttribute_redirectsAndReturnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/cs/admin/protected");

        boolean result = interceptor.preHandle(request, response, handler);

        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/admin/auth/login");
        verify(session).getAttribute("admin");
    }

    @Test
    @DisplayName("admin 속성은 있지만 role이 ADMIN이 아니면 리다이렉트 후 false 반환")
    void preHandle_wrongRole_redirectsAndReturnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn("admin1");
        when(session.getAttribute("role")).thenReturn(Role.CUSTOMER);
        when(request.getRequestURI()).thenReturn("/cs/admin/protected");

        boolean result = interceptor.preHandle(request, response, handler);

        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/admin/auth/login");
        verify(session).getAttribute("admin");
        verify(session).getAttribute("role");
    }

    @Test
    @DisplayName("admin 속성과 ADMIN role이 있으면 true 반환, 리다이렉트 호출 안 함")
    void preHandle_validAdmin_returnsTrue() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn("admin1");
        when(session.getAttribute("role")).thenReturn(Role.ADMIN);

        boolean result = interceptor.preHandle(request, response, handler);

        assertThat(result).isTrue();
        verify(response, never()).sendRedirect(anyString());
    }
}