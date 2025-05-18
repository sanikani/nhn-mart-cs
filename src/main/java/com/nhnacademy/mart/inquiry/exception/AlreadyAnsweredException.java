package com.nhnacademy.mart.inquiry.exception;

import com.nhnacademy.mart.common.exception.BadRequestException;

public class AlreadyAnsweredException extends BadRequestException {
    public AlreadyAnsweredException(String message) {
        super(message);
    }
}
