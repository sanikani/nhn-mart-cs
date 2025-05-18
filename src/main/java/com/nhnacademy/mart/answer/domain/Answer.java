package com.nhnacademy.mart.answer.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Answer {
    private Long answerId;
    private Content content;
    private LocalDateTime answeredAt;
    private String adminId;

    public Answer(Long answerId, Content content, String adminId) {
        this.answerId = answerId;
        this.content = content;
        this.answeredAt = LocalDateTime.now();
        this.adminId = adminId;
    }
}
