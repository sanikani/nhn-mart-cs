package com.nhnacademy.mart.user.customer.exception;

import com.nhnacademy.mart.common.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException() {
        super("customer.notfound");
    }
}
