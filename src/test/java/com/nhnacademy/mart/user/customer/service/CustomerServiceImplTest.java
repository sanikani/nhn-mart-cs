package com.nhnacademy.mart.user.customer.service;

import com.nhnacademy.mart.user.customer.domain.Customer;
import com.nhnacademy.mart.user.customer.exception.CustomerNotFoundException;
import com.nhnacademy.mart.user.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private static final String EXISTING_ID = "user1";
    private static final String CORRECT_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrong";


    @Test
    @DisplayName("존재하는 고객 ID와 올바른 비밀번호일 때 true 반환")
    void matches_WithCorrectPassword_ShouldReturnTrue() {
        // given
        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTING_ID)).thenReturn(customer);
        when(customer.matchPassword(CORRECT_PASSWORD)).thenReturn(true);

        // when
        boolean result = customerService.matches(EXISTING_ID, CORRECT_PASSWORD);

        // then
        assertThat(result).isTrue();

        verify(customer, times(1)).matchPassword(CORRECT_PASSWORD);
    }

    @Test
    @DisplayName("존재하는 고객 ID이지만 잘못된 비밀번호일 때 false 반환")
    void matches_WithWrongPassword_ShouldReturnFalse() {
        // given
        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTING_ID)).thenReturn(customer);
        when(customer.matchPassword(WRONG_PASSWORD)).thenReturn(false);

        // when
        boolean result = customerService.matches(EXISTING_ID, WRONG_PASSWORD);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 고객 ID로 호출 시 CustomerNotFoundException 발생")
    void matches_NonExistingId_ShouldThrowException() {
        // given
        when(customerRepository.findById("unknown")).thenReturn(null);

        // when / then
        assertThatThrownBy(() -> customerService.matches("unknown", "any"))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("isExists()는 ID 존재 여부를 정확히 반환한다")
    void isExists_ShouldReflectPresence() {
        // given
        when(customerRepository.isExistsById(EXISTING_ID)).thenReturn(true);
        when(customerRepository.isExistsById("unknown")).thenReturn(false);

        // when / then
        assertThat(customerService.isExists(EXISTING_ID)).isTrue();

        assertThat(customerService.isExists("unknown")).isFalse();
    }
}