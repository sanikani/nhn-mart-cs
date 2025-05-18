package com.nhnacademy.mart.answer.service;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.dto.AnswerRegisterRequest;

public interface AnswerService {
    void registerAnswer(String adminId, AnswerRegisterRequest answerRequest, Long inquiryId);

    Answer getAnswerById(Long answerId);
}
