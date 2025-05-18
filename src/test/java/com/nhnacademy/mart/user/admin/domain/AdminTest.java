package com.nhnacademy.mart.user.admin.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private static final String ID = "admin1";
    private static final String NAME = "관리자";
    private static final String PASSWORD = "password";
    private static final String BLANK    = "";

    @Test
    @DisplayName("생성자: 올바른 인자로 객체 생성")
    void constructor_validArguments() {
        // when
        Admin admin = new Admin(ID, NAME, PASSWORD);

        // then
        assertAll("필드 초기화",
                () -> assertEquals(ID,       admin.getId()),
                () -> assertEquals(NAME,     admin.getName()),
                () -> assertEquals(PASSWORD, admin.getPassword())
        );
    }

    @Test
    @DisplayName("생성자: null id 입력 시 IllegalArgumentException 발생")
    void constructor_nullId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(null, NAME, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 id 입력 시 IllegalArgumentException 발생")
    void constructor_blankId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(BLANK, NAME, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 공백만 있는 id 입력 시 IllegalArgumentException 발생")
    void constructor_whitespaceId_throws() {
        String whitespace = "   ";
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(whitespace, NAME, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: null name 입력 시 IllegalArgumentException 발생")
    void constructor_nullName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, null, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 name 입력 시 IllegalArgumentException 발생")
    void constructor_blankName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, BLANK, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: 공백만 있는 name 입력 시 IllegalArgumentException 발생")
    void constructor_whitespaceName_throws() {
        String whitespace = "\t ";
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, whitespace, PASSWORD)
        );
    }

    @Test
    @DisplayName("생성자: null password 입력 시 IllegalArgumentException 발생")
    void constructor_nullPassword_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, NAME, null)
        );
    }

    @Test
    @DisplayName("생성자: 빈 문자열 password 입력 시 IllegalArgumentException 발생")
    void constructor_blankPassword_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, NAME, BLANK)
        );
    }

    @Test
    @DisplayName("생성자: 공백만 있는 password 입력 시 IllegalArgumentException 발생")
    void constructor_whitespacePassword_throws() {
        String whitespace = "  ";
        assertThrows(IllegalArgumentException.class,
                () -> new Admin(ID, NAME, whitespace)
        );
    }
}