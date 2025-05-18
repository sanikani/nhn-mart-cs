package com.nhnacademy.mart.inquiry.exception;

import com.nhnacademy.mart.common.exception.NotFoundException;

public class InquiryNotFoundException extends NotFoundException {
    public InquiryNotFoundException() {
        super("inquiry.notfound");
    }
}
