package com.nhnacademy.mart.inquiry.controller;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.service.AnswerService;
import com.nhnacademy.mart.inquiry.domain.*;
import com.nhnacademy.mart.inquiry.dto.InquiryRegisterRequest;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InquiryController.class)
class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @MockitoBean
    private AnswerService answerService;

    MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void setUp() {
        session.setAttribute("id", "user");
    }

    @Test
    @DisplayName("GET /cs/inquiry - 문의 등록 폼 뷰 호출")
    void inquiryRegisterForm() throws Exception {
        mockMvc.perform(get("/cs/inquiry").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryRegisterForm"));

        verifyNoInteractions(inquiryService, answerService);
    }

    @Test
    @DisplayName("POST /cs/inquiry - 정상 등록 후 /cs로 리다이렉트하고 서비스 호출")
    void inquiryRegister_success() throws Exception {
        mockMvc.perform(post("/cs/inquiry")
                        .session(session)
                        .param("title", "테스트 제목")
                        .param("content", "테스트 내용")
                        .param("category", Category.OTHER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));

        verify(inquiryService).registerInquiry(any(InquiryRegisterRequest.class), anyString());
    }

    @Test
    @DisplayName("GET /cs/inquiry/{inquiryId} - 답변 없는 상태(Waiting) 상세 조회")
    void inquiryDetail_waiting() throws Exception {
        Long id = 10L;
        Inquiry inquiry = new Inquiry(
                id,
                new Title("제목"),
                new Content("내용"),
                Category.OTHER,
                "user1",
                Collections.emptyList()
        );
        when(inquiryService.getInquiryById(id)).thenReturn(inquiry);

        mockMvc.perform(get("/cs/inquiry/{inquiryId}", id).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryDetail"))
                .andExpect(model().attribute("inquiry", inquiry))
                .andExpect(model().attributeDoesNotExist("answer"));

        verify(inquiryService).getInquiryById(id);
        verifyNoInteractions(answerService);
    }

    @Test
    @DisplayName("GET /cs/inquiry/{inquiryId} - 답변 완료 상태(Answered) 상세 조회")
    void inquiryDetail_answered() throws Exception {
        Long id = 20L;
        Inquiry inquiry = new Inquiry(
                id,
                new Title("제목2"),
                new Content("내용2"),
                Category.OTHER,
                "user2",
                Collections.emptyList()
        );
        long answerId = 55L;
        inquiry.registerAnswer(answerId);

        Answer answer = new Answer(answerId, new com.nhnacademy.mart.answer.domain.Content("답변 내용"), "admin1");
        when(inquiryService.getInquiryById(id)).thenReturn(inquiry);
        when(answerService.getAnswerById(answerId)).thenReturn(answer);

        mockMvc.perform(get("/cs/inquiry/{inquiryId}", id).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryDetail"))
                .andExpect(model().attribute("inquiry", inquiry))
                .andExpect(model().attribute("answer", answer));

        verify(inquiryService).getInquiryById(id);
        verify(answerService).getAnswerById(answerId);
    }
}
