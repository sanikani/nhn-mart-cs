package com.nhnacademy.mart.user.admin.repository;

import com.nhnacademy.mart.user.admin.domain.Admin;

public interface AdminRepository {
    Admin findById(String id);

    void save(Admin admin);

    boolean isExists(String id);
}
