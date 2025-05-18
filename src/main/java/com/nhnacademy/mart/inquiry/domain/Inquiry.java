package com.nhnacademy.mart.inquiry.domain;

import com.nhnacademy.mart.inquiry.exception.AlreadyAnsweredException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Inquiry {

    private Long id;
    private Title title;
    private Content content;
    private Category category;
    private InquiryStatus status;
    private LocalDateTime createDate;
    private String customerId;
    private List<Attachment> attachments;
    private Long answerId;

    public Inquiry(Long id, Title title, Content content, Category category,
                   String customerId, List<Attachment> attachments) {
        setId(id);
        setTitle(title);
        setContent(content);
        setCategory(category);
        setCustomerId(customerId);
        setAttachments(attachments);
        this.status = InquiryStatus.WAITING;
        this.createDate = LocalDateTime.now();
    }

    private void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("no category");
        }
        this.category = category;
    }

    private void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("no id");
        }
        this.id = id;
    }

    private void setTitle(Title title) {
        if (title == null) {
            throw new IllegalArgumentException("no title");
        }
        this.title = title;
    }

    private void setContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("no content");
        }
        this.content = content;
    }


    private void setCustomerId(String customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("no customerId");
        }
        this.customerId = customerId;
    }

    private void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void registerAnswer(Long answerId) {
        changeInquiryStatus(InquiryStatus.ANSWERED);
        this.answerId = answerId;
    }

    public void changeInquiryStatus(InquiryStatus status) {
        verifyNotAnswered();
        this.status = status;
    }

    private void verifyNotAnswered() {
        if (status == InquiryStatus.ANSWERED) {
            throw new AlreadyAnsweredException("inquiry.already.answered");
        }
    }
}
