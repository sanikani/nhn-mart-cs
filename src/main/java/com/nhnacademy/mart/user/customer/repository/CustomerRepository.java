package com.nhnacademy.mart.user.customer.repository;

import com.nhnacademy.mart.user.customer.domain.Customer;

public interface CustomerRepository {

    Customer findById(String id);

    void save(Customer customer);

    boolean isExistsById(String id);
}
