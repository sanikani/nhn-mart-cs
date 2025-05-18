package com.nhnacademy.mart.inquiry.controller;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InquiryIndexController {

    private final InquiryService inquiryService;

    @GetMapping("/cs")
    public String getAllInquiries(HttpServletRequest httpServletRequest, Model model, @RequestParam(required = false) Category category) {
        String id = (String) httpServletRequest.getSession(false).getAttribute("id");
        List<Inquiry> inquiries = inquiryService.getAllInquiries(id, category);
        model.addAttribute("inquiries", inquiries);
        return "index";
    }
}
