package com.nhnacademy.mart.answer.dto;

import com.nhnacademy.mart.answer.domain.Content;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AnswerRegisterRequest {

    @NotNull
    @Size(min = 1, max = 40000)
    String content;

    public Content getContent() {
        return new Content(content);
    }
}
