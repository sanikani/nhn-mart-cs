package com.nhnacademy.mart.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private static final String ERROR_VIEW_NAME = "error";
    public static final String EXCEPTION_ATTRIBUTE_NAME = "exception";

    private final MessageSource messageSource;
    private final HttpServletResponse httpServletResponse;

    @ExceptionHandler(ApiException.class)
    public String handleApiException(ApiException e, Model model, Locale locale) {
        log.warn("예외 발생: {}", e.getMessageCode(), e);

        String message = messageSource.getMessage(e.getMessageCode(), null, locale);
        httpServletResponse.setStatus(e.getHttpStatus().value());
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, message);
        return ERROR_VIEW_NAME;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, errorMessages);
        return ERROR_VIEW_NAME;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerError(RuntimeException e, Model model) {
        log.error(ERROR_VIEW_NAME, e);
        model.addAttribute(EXCEPTION_ATTRIBUTE_NAME, "서버 내부 오류 발생");
        return ERROR_VIEW_NAME;
    }
}