package com.nhnacademy.mart.user.admin.service;

import com.nhnacademy.mart.inquiry.exception.AdminNotFoundException;
import com.nhnacademy.mart.user.admin.domain.Admin;
import com.nhnacademy.mart.user.admin.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private final String EXISTING_ID = "admin1";
    private final String ADMIN_NAME = "admin";
    private final String CORRECT_PASSWORD = "pass";
    private final String WRONG_PASSWORD = "bad";

    @Test
    @DisplayName("존재하는 관리자 ID와 올바른 비밀번호일 때 true 반환")
    void matches_WithCorrectPassword_ShouldReturnTrue() {
        // given
        Admin admin = new Admin(EXISTING_ID, ADMIN_NAME, CORRECT_PASSWORD);
        when(adminRepository.findById(EXISTING_ID)).thenReturn(admin);

        // when
        boolean result = adminService.matches(EXISTING_ID, CORRECT_PASSWORD);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("존재하는 관리자 ID이지만 잘못된 비밀번호일 때 false 반환")
    void matches_WithWrongPassword_ShouldReturnFalse() {
        // given
        Admin admin = new Admin(EXISTING_ID, ADMIN_NAME, CORRECT_PASSWORD);
        when(adminRepository.findById(EXISTING_ID)).thenReturn(admin);

        // when
        boolean result = adminService.matches(EXISTING_ID, WRONG_PASSWORD);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 관리자 ID로 호출 시 AdminNotFoundException 발생")
    void matches_NonExistingId_ShouldThrowException() {
        // given
        when(adminRepository.findById("unknown")).thenReturn(null);

        // when / then
        assertThatThrownBy(() -> adminService.matches("unknown", "any"))
                .isInstanceOf(AdminNotFoundException.class);
    }
}
