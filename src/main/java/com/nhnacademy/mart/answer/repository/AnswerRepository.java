package com.nhnacademy.mart.answer.repository;

import com.nhnacademy.mart.answer.domain.Answer;

public interface AnswerRepository {
    Long nextOrderId();

    Answer save(Answer answer);

    Answer findById(Long answerId);
}
