package com.nhnacademy.mart.user.customer.repository;

import com.nhnacademy.mart.user.customer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<String, Customer> customerMap = new HashMap<>();

    public CustomerRepositoryImpl() {
        save(new Customer("test1", "테스터", "1111"));
        save(new Customer("test2", "테스터2", "0000"));
    }

    @Override
    public Customer findById(String id) {
        return customerMap.get(id);
    }

    @Override
    public void save(Customer customer) {
        customerMap.put(customer.getId(), customer);
    }

    @Override
    public boolean isExistsById(String id) {
        return customerMap.containsKey(id);
    }
}
