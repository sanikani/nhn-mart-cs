package com.nhnacademy.mart.answer.repository;

import com.nhnacademy.mart.answer.domain.Answer;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Repository
public class AnswerRepositoryImpl implements AnswerRepository{

    private final Map<Long, Answer> answerMap = new HashMap<>();

    @Override
    public Long nextOrderId() {
        return answerMap.keySet()
                .stream()
                .max(Comparator.comparing(Function.identity()))
                .map(l -> l + 1)
                .orElse(1L);
    }

    @Override
    public Answer save(Answer answer) {
        answerMap.put(answer.getAnswerId(), answer);
        return answer;
    }

    @Override
    public Answer findById(Long answerId) {
        return answerMap.get(answerId);
    }
}
