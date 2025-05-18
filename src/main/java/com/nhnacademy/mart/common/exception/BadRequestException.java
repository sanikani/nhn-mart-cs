package com.nhnacademy.mart.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException{
    public BadRequestException(String messageCode) {
        super(messageCode, HttpStatus.BAD_REQUEST);
    }
}
