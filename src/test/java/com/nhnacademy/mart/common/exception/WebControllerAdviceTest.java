package com.nhnacademy.mart.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebControllerAdviceTest {

    MessageSource messageSource;
    HttpServletResponse rawResponse;
    WebControllerAdvice advice;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        rawResponse   = new MockHttpServletResponse();
        advice = new WebControllerAdvice(messageSource, rawResponse);
    }

    @Test
    @DisplayName("handleApiException: ApiException 발생 시 메시지 조회, 상태 설정, model에 'exception', 뷰 'error' 반환")
    void handleApiException() {
        ApiException e = new ApiException("error.code", HttpStatus.NOT_FOUND);
        ConcurrentModel model = new ConcurrentModel();
        when(messageSource.getMessage(e.getMessageCode(), null, Locale.KOREAN)).thenReturn("에러 발생");

        String viewName = advice.handleApiException(e, model, Locale.KOREAN);

        assertThat(viewName).isEqualTo("error");
        assertThat(rawResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(model.asMap()).containsEntry(WebControllerAdvice.EXCEPTION_ATTRIBUTE_NAME, "에러 발생");
    }

    @Test
    @DisplayName("handleValidation: MethodArgumentNotValidException 발생 시 모델에 합쳐진 필드 에러 메시지, 뷰 'error' 반환")
    void handleValidation() {
        // given: DTO 이름과 바인딩 결과 준비
        Dummy dto = new Dummy();
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(dto, "dto");
        br.addError(new FieldError("dto", "field1", "error1"));
        br.addError(new FieldError("dto", "field2", "error2"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, br);

        ConcurrentModel model = new ConcurrentModel();

        // when
        String viewName = advice.handleValidation(ex, model);

        // then
        assertThat(viewName).isEqualTo("error");
        String combined = (String) model.asMap().get(WebControllerAdvice.EXCEPTION_ATTRIBUTE_NAME);
        assertThat(combined)
                .contains("field1: error1")
                .contains("field2: error2")
                .contains(" | ");
    }

    @Test
    @DisplayName("handleServerError: RuntimeException 발생 시 status 500, 모델에 '서버 내부 오류 발생', 뷰 'error' 반환")
    void handleServerError() {
        // given
        RuntimeException ex = new IllegalStateException("boom");
        ConcurrentModel model = new ConcurrentModel();

        // when
        String viewName = advice.handleServerError(ex, model);

        // then
        assertThat(viewName).isEqualTo("error");
        assertThat(model.asMap())
                .containsEntry(WebControllerAdvice.EXCEPTION_ATTRIBUTE_NAME, "서버 내부 오류 발생");
    }

    private static class Dummy {}
}