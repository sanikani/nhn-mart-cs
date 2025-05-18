package com.nhnacademy.mart.inquiry.controller;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.service.AnswerService;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.domain.InquiryStatus;
import com.nhnacademy.mart.inquiry.dto.InquiryRegisterRequest;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/cs/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final AnswerService answerService;

    @GetMapping
    public String inquiryRegisterForm() {
        return "inquiryRegisterForm";
    }

    @PostMapping
    public String inquiryRegister(@Valid @ModelAttribute InquiryRegisterRequest inquiryRequest, HttpServletRequest httpServletRequest) {
        String id = (String) httpServletRequest.getSession().getAttribute("id");
        log.info(inquiryRequest.toString());
        inquiryService.registerInquiry(inquiryRequest, id);
        return "redirect:/cs";
    }

    @GetMapping("/{inquiryId}")
    public String inquiryDetail(@PathVariable Long inquiryId, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        if (inquiry.getStatus().equals(InquiryStatus.ANSWERED)) {
            Answer answer = answerService.getAnswerById(inquiry.getAnswerId());
            model.addAttribute("answer", answer);
        }

        model.addAttribute("inquiry", inquiry);
        log.info(inquiry.getAttachments().toString());
        return "inquiryDetail";
    }
}

