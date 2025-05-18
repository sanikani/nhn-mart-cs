package com.nhnacademy.mart.user.admin.service;

import com.nhnacademy.mart.inquiry.exception.AdminNotFoundException;
import com.nhnacademy.mart.user.admin.domain.Admin;
import com.nhnacademy.mart.user.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public boolean matches(String id, String password) {
        Admin admin = adminRepository.findById(id);

        if (admin == null) {
            throw new AdminNotFoundException();
        }

        return admin.getPassword().equals(password);
    }
}
