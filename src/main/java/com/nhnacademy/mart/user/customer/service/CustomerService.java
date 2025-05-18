package com.nhnacademy.mart.user.customer.service;

public interface CustomerService {

    boolean matches(String id, String password);

    boolean isExists(String id);
}
