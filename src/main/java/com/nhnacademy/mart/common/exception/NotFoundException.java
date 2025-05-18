package com.nhnacademy.mart.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException{
    public NotFoundException(String messageCode) {
        super(messageCode, HttpStatus.NOT_FOUND);
    }
}
