package com.nhnacademy.mart.user.customer.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class Customer {

    private String id;
    private String name;
    private String password;

    public Customer(String id, String name, String password) {
        setId(id);
        setName(name);
        setPassword(password);
    }

    private void setName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("no name");
        }
        this.name = name;
    }

    private void setPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("no password");
        }
        this.password = password;
    }

    private void setId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("no id");
        }
        this.id = id;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
