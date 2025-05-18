package com.nhnacademy.mart.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Slf4j
@Component
@Getter
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            log.info("로그인 필요.{} redirect:/cs/auth/login",request.getRequestURI());
            response.sendRedirect("/cs/auth/login");
            return false;
        }

        return true;
    }
}
