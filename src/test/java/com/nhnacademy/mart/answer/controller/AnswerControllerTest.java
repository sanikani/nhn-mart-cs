package com.nhnacademy.mart.answer.controller;

import com.nhnacademy.mart.answer.service.AnswerService;
import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Content;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.domain.Title;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import com.nhnacademy.mart.user.auth.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnswerService answerService;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("GET /cs/admin/answer/{inquiryId} - 답변 폼과 뷰 모델에 Inquiry가 담겨야 함")
    void answerForm() throws Exception {
        Long inquiryId = 1L;
        Inquiry inquiry = new Inquiry(inquiryId,
                new Title("title"),
                new Content("content"),
                Category.OTHER,
                "customer",
                null);
        String adminId = "admin123";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("admin", adminId);
        session.setAttribute("role", Role.ADMIN);
        when(inquiryService.getInquiryById(inquiryId)).thenReturn(inquiry);

        mockMvc.perform(get("/cs/admin/answer/{inquiryId}", inquiryId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/answerForm"))
                .andExpect(model().attribute("inquiry", inquiry));
        verify(inquiryService).getInquiryById(inquiryId);
    }

    @Test
    @DisplayName("POST /cs/admin/answer/{inquiryId} - 성공 시 /cs/admin으로 리다이렉트하고 서비스 호출")
    void submitAnswer() throws Exception {
        // given
        Long inquiryId = 2L;
        String adminId = "admin123";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("admin", adminId);
        session.setAttribute("role", Role.ADMIN);

        // when & then
        mockMvc.perform(post("/cs/admin/answer/{inquiryId}", inquiryId)
                        .session(session)
                        .param("content", "테스트 답변 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));

        // then
        verify(answerService).registerAnswer(
                eq(adminId),
                argThat(req -> "테스트 답변 내용".equals(req.getContent().getValue())),
                eq(inquiryId)
        );
    }
}