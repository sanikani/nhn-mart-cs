package com.nhnacademy.mart.common.interceptor;

import com.nhnacademy.mart.user.auth.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null || !session.getAttribute("role").equals(Role.ADMIN)) {
            log.info("관리자 인증 필요.{} redirect:/cs/admin/auth/login",request.getRequestURI());
            response.sendRedirect("/cs/admin/auth/login");
            return false;
        }
        return true;
    }
}
