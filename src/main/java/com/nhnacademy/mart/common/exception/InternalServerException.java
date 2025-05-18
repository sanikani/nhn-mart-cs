package com.nhnacademy.mart.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends ApiException {

    public InternalServerException(String messageCode) {
        super(messageCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
