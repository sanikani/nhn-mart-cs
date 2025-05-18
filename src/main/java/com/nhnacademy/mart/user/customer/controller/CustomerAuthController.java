package com.nhnacademy.mart.user.customer.controller;

import com.nhnacademy.mart.user.auth.Role;
import com.nhnacademy.mart.user.customer.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/auth")
public class CustomerAuthController {

    private final CustomerService customerService;

    @GetMapping("/login")
    public String loginForm(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            String id = (String) session.getAttribute("id");
            if (StringUtils.hasText(id)) {
                return "redirect:/cs";
            }
        }
        return "loginForm";
    }

    @PostMapping("/login")
    public String doLogin(String id, String password, HttpServletRequest httpServletRequest) {
        if (customerService.matches(id, password)) {
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("id", id);
            session.setAttribute("role", Role.CUSTOMER);
            return "redirect:/cs";
        }
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession(false).invalidate();
        return "index";
    }
}
