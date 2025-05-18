package com.nhnacademy.mart.inquiry.dto;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Content;
import com.nhnacademy.mart.inquiry.domain.Title;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class InquiryRegisterRequest {

    @NotNull
    @Size(min = 2, max = 200)
    private final String title;

    @NotNull
    @Length(max = 40000)
    private final String content;

    @NotNull
    private final Category category;

    private final List<MultipartFile> attachments;

    public InquiryRegisterRequest(String title, String content, Category category, List<MultipartFile> attachments) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "InquiryRegisterRequest{" +
                "title=" + title +
                ", content=" + content +
                ", category=" + category +
                ", attachments=" + attachments +
                '}';
    }

    public Content getContent() {
        return new Content(content);
    }

    public Title getTitle() {
        return new Title(title);
    }
}
