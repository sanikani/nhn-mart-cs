package com.nhnacademy.mart.inquiry.exception;

import com.nhnacademy.mart.common.exception.NotFoundException;

public class AdminNotFoundException extends NotFoundException {
    public AdminNotFoundException() {
        super("admin.notfound");
    }
}
