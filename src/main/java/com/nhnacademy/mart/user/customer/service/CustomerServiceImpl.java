package com.nhnacademy.mart.user.customer.service;

import com.nhnacademy.mart.user.customer.domain.Customer;
import com.nhnacademy.mart.user.customer.exception.CustomerNotFoundException;
import com.nhnacademy.mart.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public boolean matches(String id, String password) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        return customer.matchPassword(password);
    }

    @Override
    public boolean isExists(String id) {
        return customerRepository.isExistsById(id);
    }

}
