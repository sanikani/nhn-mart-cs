package com.nhnacademy.mart.user.customer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private static final String ID = "user1";
    public static final String NAME = "홍길동";
    public static final String PASSWORD = "secret";
    public static final String BLANK = "";

    @Test
    @DisplayName("생성자: 올바른 인자로 객체 생성")
    void constructor_validArguments() {
        Customer customer = new Customer(ID, NAME, PASSWORD);

        assertAll("필드 초기화",
                () -> assertEquals(ID,       customer.getId()),
                () -> assertEquals(NAME,     customer.getName()),
                () -> assertEquals(PASSWORD, customer.getPassword())
        );
    }

    @Test
    @DisplayName("생성자: null id 입력 시 IllegalArgumentException 발생")
    void constructor_nullId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(null, NAME, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 id 입력 시 IllegalArgumentException 발생")
    void constructor_blankId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(BLANK, NAME, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: null name 입력 시 IllegalArgumentException 발생")
    void constructor_nullName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(ID, null, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 name 입력 시 IllegalArgumentException 발생")
    void constructor_blankName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(ID, BLANK, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: null password 입력 시 IllegalArgumentException 발생")
    void constructor_nullPassword_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(ID, NAME, null)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 password 입력 시 IllegalArgumentException 발생")
    void constructor_blankPassword_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(ID, NAME, BLANK)
        );
    }

    @Test
    @DisplayName("matchPassword: 올바른 비밀번호 검증은 true, 잘못된 비밀번호 검증은 false")
    void matchPassword_behavior() {
        Customer customer = new Customer(ID, NAME, PASSWORD);

        assertTrue(customer.matchPassword("secret"));
        assertFalse(customer.matchPassword("wrong"));
    }
}