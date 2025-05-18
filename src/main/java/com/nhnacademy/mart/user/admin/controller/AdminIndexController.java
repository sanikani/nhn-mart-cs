package com.nhnacademy.mart.user.admin.controller;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cs/admin")
@RequiredArgsConstructor
public class AdminIndexController {

    private final InquiryService inquiryService;

    @GetMapping
    public String adminIndex(Model model, @RequestParam(required = false)Category category) {
        List<Inquiry> inquiries = inquiryService.getNotAnsweredInquiries(category);
        model.addAttribute("inquiries", inquiries);
        return "admin/index";
    }
}
