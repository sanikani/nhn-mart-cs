package com.nhnacademy.mart.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException{

    public UnauthorizedException(String messageCode) {
        super(messageCode, HttpStatus.UNAUTHORIZED);
    }
}
