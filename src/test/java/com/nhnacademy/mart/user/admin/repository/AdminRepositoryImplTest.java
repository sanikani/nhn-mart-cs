package com.nhnacademy.mart.user.admin.repository;

import com.nhnacademy.mart.user.admin.domain.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AdminRepositoryImplTest {

    private AdminRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new AdminRepositoryImpl();
    }

    @Test
    @DisplayName("초기 생성 시 기본 관리자(admin1, admin2)가 등록되어 있어야 한다")
    void initialAdmins_ShouldBePresent() {
        Admin a1 = repository.findById("admin1");
        Admin a2 = repository.findById("admin2");

        assertThat(a1)
                .isNotNull()
                .extracting(Admin::getId, Admin::getPassword)
                .containsExactly("admin1", "admin");

        assertThat(a2)
                .isNotNull()
                .extracting(Admin::getId, Admin::getPassword)
                .containsExactly("admin2", "admin");
    }

    @Test
    @DisplayName("없는 ID를 조회하면 null을 반환한다")
    void findById_UnknownId_ShouldReturnNull() {
        assertThat(repository.findById("no_such_admin"))
                .isNull();
    }

    @Test
    @DisplayName("존재 여부를 반환한다")
    void isExists_ShouldReflectPresence() {
        assertThat(repository.isExists("admin1"))
                .isTrue();

        assertThat(repository.isExists("unknown"))
                .isFalse();
    }

    @Test
    @DisplayName("신규 관리자 추가")
    void save_NewAdmin_ShouldBeRetrievable() {
        Admin newAdmin = new Admin("admin", "admin", "admin");
        repository.save(newAdmin);

        assertThat(repository.isExists("admin"))
                .isTrue();

        assertThat(repository.findById("admin"))
                .isEqualTo(newAdmin);
    }
}