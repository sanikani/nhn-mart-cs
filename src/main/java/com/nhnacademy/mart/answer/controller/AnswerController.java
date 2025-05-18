package com.nhnacademy.mart.answer.controller;

import com.nhnacademy.mart.answer.dto.AnswerRegisterRequest;
import com.nhnacademy.mart.answer.service.AnswerService;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/admin/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final InquiryService inquiryService;

    @GetMapping("/{inquiryId}")
    public String answerForm(@PathVariable Long inquiryId,
                             Model model) {
        model.addAttribute("inquiry", inquiryService.getInquiryById(inquiryId));
        return "/admin/answerForm";
    }

    @PostMapping("/{inquiryId}")
    public String answer(@PathVariable Long inquiryId,
                         @Valid @ModelAttribute AnswerRegisterRequest answerRequest,
                         HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String adminId = (String) session.getAttribute("admin");
        answerService.registerAnswer(adminId, answerRequest, inquiryId);
        return "redirect:/cs/admin";
    }
}
