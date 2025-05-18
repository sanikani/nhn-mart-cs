package com.nhnacademy.mart.answer.service;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.domain.Content;
import com.nhnacademy.mart.answer.dto.AnswerRegisterRequest;
import com.nhnacademy.mart.answer.repository.AnswerRepository;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.exception.InquiryNotFoundException;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private InquiryService inquiryService;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private final String adminId = "admin01";
    private final Long inquiryId = 42L;


    @Test
    @DisplayName("registerAnswer: 정상 흐름 - 답변 저장 및 문의에 등록")
    void registerAnswer_success() {
        Inquiry mockInquiry = mock(Inquiry.class);
        when(inquiryService.getInquiryById(inquiryId)).thenReturn(mockInquiry);
        when(answerRepository.nextOrderId()).thenReturn(100L);

        AnswerRegisterRequest request = new AnswerRegisterRequest("테스트 내용");

        Answer savedAnswer = new Answer(100L, request.getContent(), adminId);
        when(answerRepository.save(any(Answer.class))).thenReturn(savedAnswer);

        Answer answer = answerService.registerAnswer(adminId, request, inquiryId);

        assertEquals(100L, answer.getAnswerId());
        assertEquals("테스트 내용", answer.getContent().getValue());
        assertEquals(adminId, answer.getAdminId());

        verify(mockInquiry).registerAnswer(100L);
    }

    @Test
    @DisplayName("registerAnswer: 문의가 없으면 InquiryNotFoundException 발생")
    void registerAnswer_inquiryNotFound() {
        when(inquiryService.getInquiryById(inquiryId)).thenReturn(null);
        AnswerRegisterRequest request = new AnswerRegisterRequest("내용");

        assertThrows(InquiryNotFoundException.class,
                () -> answerService.registerAnswer(adminId, request, inquiryId));

        verify(answerRepository, never()).save(any());
    }

    @Test
    @DisplayName("getAnswerById: repository.findById 호출 결과 반환")
    void getAnswerById_delegation() {
        Answer expected = new Answer(1L, new Content("답변"), "admin");
        when(answerRepository.findById(1L)).thenReturn(expected);

        Answer actual = answerService.getAnswerById(1L);

        assertSame(expected, actual);
        verify(answerRepository).findById(1L);
    }

}