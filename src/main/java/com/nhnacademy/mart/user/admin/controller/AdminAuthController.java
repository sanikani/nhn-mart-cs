package com.nhnacademy.mart.user.admin.controller;

import com.nhnacademy.mart.user.admin.service.AdminService;
import com.nhnacademy.mart.user.auth.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cs/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginForm(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            String id = (String) session.getAttribute("admin");
            if (StringUtils.hasText(id)) {
                return "redirect:/cs/admin";
            }
        }
        return "adminLoginForm";
    }

    @PostMapping("/login")
    public String doLogin(String id, String password, HttpServletRequest httpServletRequest) {
        if (adminService.matches(id, password)) {
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("admin", id);
            session.setAttribute("role", Role.ADMIN);
            return "redirect:/cs/admin";
        }
        return "adminLoginForm";
    }
}
