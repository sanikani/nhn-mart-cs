package com.nhnacademy.mart.common.config;

import com.nhnacademy.mart.common.interceptor.AdminCheckInterceptor;
import com.nhnacademy.mart.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());

        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/cs/auth/login",
                        "/error",
                        "/cs/admin/**",
                        "/attachments/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/img/**",
                        "/favicon.ico",
                        "/webjars/**",
                        "/.well-known/**"
                );

        registry.addInterceptor(new AdminCheckInterceptor())
                .addPathPatterns("/cs/admin/**")
                .excludePathPatterns(
                        "/cs/admin/auth/login"
                );
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("messages", "errors");

        return messageSource;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
