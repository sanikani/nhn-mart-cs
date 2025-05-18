package com.nhnacademy.mart.user.admin.repository;

import com.nhnacademy.mart.user.admin.domain.Admin;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

    private final Map<String, Admin> adminMap = new HashMap<>();

    public AdminRepositoryImpl() {
        save(new Admin("admin1", "admin1", "admin"));
        save(new Admin("admin2", "admin2", "admin"));
    }

    @Override
    public Admin findById(String id) {
        return adminMap.get(id);
    }

    @Override
    public void save(Admin admin) {
        adminMap.put(admin.getId(), admin);
    }

    @Override
    public boolean isExists(String id) {
        return adminMap.containsKey(id);
    }
}
