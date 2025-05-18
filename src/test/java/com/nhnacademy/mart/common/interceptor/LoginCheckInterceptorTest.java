package com.nhnacademy.mart.common.interceptor;

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
class LoginCheckInterceptorTest {

    @InjectMocks
    private LoginCheckInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private HttpSession session;

    @Test
    @DisplayName("세션이 없으면 로그인 페이지로 리다이렉트 후 false 반환")
    void preHandle_noSession_redirectsAndReturnsFalse() throws Exception {
        // given
        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/cs/protected/resource");

        // when
        boolean result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/auth/login");
        verify(request).getSession(false);
        verify(request).getRequestURI();
    }

    @Test
    @DisplayName("세션은 있지만 id 속성이 없으면 로그인 페이지로 리다이렉트 후 false 반환")
    void preHandle_sessionWithoutId_redirectsAndReturnsFalse() throws Exception {
        // given
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/cs/protected");

        // when
        boolean result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/auth/login");
        verify(session).getAttribute("id");
    }

    @Test
    @DisplayName("세션 및 id 속성이 있으면 true 반환하고 리다이렉트 호출 안 함")
    void preHandle_sessionWithId_returnsTrue() throws Exception {
        // given
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("id")).thenReturn("user42");

        // when
        boolean result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
        verify(response, never()).sendRedirect(anyString());
    }
}