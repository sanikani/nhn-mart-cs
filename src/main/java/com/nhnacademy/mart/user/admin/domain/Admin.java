package com.nhnacademy.mart.user.admin.domain;

import lombok.Getter;

@Getter
public class Admin {
    private String id;
    private String name;
    private String password;

    public Admin(String id, String name, String password) {
        setId(id);
        setName(name);
        setPassword(password);
    }

    private void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("no id");
        }
        this.id = id;
    }

    private void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("no name");
        }
        this.name = name;
    }

    private void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("no password");
        }
        this.password = password;
    }
}
