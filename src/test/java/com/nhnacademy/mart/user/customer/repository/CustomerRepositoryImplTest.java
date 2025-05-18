package com.nhnacademy.mart.user.customer.repository;

import com.nhnacademy.mart.user.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CustomerRepositoryImplTest {

    private CustomerRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new CustomerRepositoryImpl();
    }

    @Test
    @DisplayName("초기 생성 시 기본 고객이 등록되어 있어야 한다")
    void initialCustomers_ShouldBePresent() {
        Customer c1 = repository.findById("test1");
        Customer c2 = repository.findById("test2");

        assertThat(c1)
                .isNotNull()
                .extracting(Customer::getId, Customer::getName, Customer::getPassword)
                .containsExactly("test1", "테스터", "1111");

        assertThat(c2)
                .isNotNull()
                .extracting(Customer::getId, Customer::getName, Customer::getPassword)
                .containsExactly("test2", "테스터2", "0000");
    }

    @Test
    @DisplayName("findById()에 없는 ID를 조회하면 null을 반환한다")
    void findById_UnknownId_ShouldReturnNull() {
        assertThat(repository.findById("no_such_user"))
                .isNull();
    }

    @Test
    @DisplayName("isExistsById()는 존재 여부를 반환한다")
    void isExistsById_ShouldReflectPresence() {
        assertThat(repository.isExistsById("test1"))
                .isTrue();

        assertThat(repository.isExistsById("unknown"))
                .isFalse();
    }

    @Test
    @DisplayName("save()로 신규 고객을 추가할 수 있다")
    void save_NewCustomer_ShouldBeRetrievable() {
        Customer newCustomer = new Customer("john", "John Doe", "pwd123");
        repository.save(newCustomer);

        assertThat(repository.isExistsById("john"))
                .isTrue();

        assertThat(repository.findById("john"))
                .isEqualTo(newCustomer);
    }
}
