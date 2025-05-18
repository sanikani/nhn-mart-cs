package com.nhnacademy.mart.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String messageCode;

    public ApiException(String messageCode, HttpStatus httpStatus) {
        super(messageCode);
        this.messageCode = messageCode;
        this.httpStatus = httpStatus;
    }
}
