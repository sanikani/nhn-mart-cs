package com.nhnacademy.mart.inquiry.domain;

import com.nhnacademy.mart.inquiry.exception.AlreadyAnsweredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InquiryTest {

    private final Long id = 1L;
    private final Title title = new Title("t");
    private final Content content = new Content("c");
    private final Category category = Category.OTHER;
    private final String customerId = "u";
    private final List<Attachment> attachments = Collections.emptyList();

    @Test
    @DisplayName("생성자: 정상 파라미터로 객체 생성")
    void constructor_validArguments() {
        Inquiry inquiry = new Inquiry(
                1L,
                title,
                content,
                category,
                customerId,
                Collections.emptyList()
        );

        assertAll("initial state",
                () -> assertEquals(1L, inquiry.getId()),
                () -> assertSame(title, inquiry.getTitle()),
                () -> assertSame(content, inquiry.getContent()),
                () -> assertEquals(category, inquiry.getCategory()),
                () -> assertEquals(customerId, inquiry.getCustomerId()),
                () -> assertEquals(InquiryStatus.WAITING, inquiry.getStatus()),
                () -> assertNull(inquiry.getAnswerId()),
                () -> assertNotNull(inquiry.getCreateDate()),
                () -> assertEquals(0, inquiry.getAttachments().size())
        );
    }

    @Test
    @DisplayName("생성자: id가 null이면 IllegalArgumentException")
    void constructor_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inquiry(null, title, content, category, customerId, attachments)
        );
    }

    @Test
    @DisplayName("생성자: title이 null이면 IllegalArgumentException")
    void constructor_nullTitle_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inquiry(id, null, content, category, customerId, attachments)
        );
    }

    @Test
    @DisplayName("생성자: content가 null이면 IllegalArgumentException")
    void constructor_nullContent_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inquiry(id, title, null, category, customerId, attachments)
        );
    }

    @Test
    @DisplayName("생성자: category가 null이면 IllegalArgumentException")
    void constructor_nullCategory_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inquiry(id, title, content, null, customerId, attachments)
        );
    }

    @Test
    @DisplayName("생성자: customerId가 null이면 IllegalArgumentException")
    void constructor_nullCustomerId_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inquiry(id, title, content, category, null, attachments)
        );
    }

    @Test
    @DisplayName("changeInquiryStatus: WAITING 상태에서 ANSWERED로 변경")
    void changeStatus_fromWaiting_toAnswered() {
        Inquiry inquiry = new Inquiry(
                2L,
                new Title("t2"),
                new Content("c2"),
                Category.OTHER,
                "u2",
                Collections.emptyList()
        );

        inquiry.changeInquiryStatus(InquiryStatus.ANSWERED);

        assertEquals(InquiryStatus.ANSWERED, inquiry.getStatus());
    }

    @Test
    @DisplayName("registerAnswer: status ANSWERED, answerId 설정")
    void registerAnswer_setsStatusAndAnswerId() {
        Inquiry inquiry = new Inquiry(
                3L,
                new Title("t3"),
                new Content("c3"),
                Category.OTHER,
                "u3",
                Collections.emptyList()
        );

        inquiry.registerAnswer(99L);

        assertAll("after registerAnswer",
                () -> assertEquals(InquiryStatus.ANSWERED, inquiry.getStatus()),
                () -> assertEquals(99L, inquiry.getAnswerId())
        );
    }

    @Test
    @DisplayName("changeInquiryStatus: 이미 ANSWERED 상태면 AlreadyAnsweredException")
    void changeStatus_whenAlreadyAnswered_throws() {
        Inquiry inquiry = new Inquiry(
                4L,
                new Title("t4"),
                new Content("c4"),
                Category.OTHER,
                "u4",
                Collections.emptyList()
        );

        inquiry.registerAnswer(100L);

        AlreadyAnsweredException ex = assertThrows(
                AlreadyAnsweredException.class,
                () -> inquiry.changeInquiryStatus(InquiryStatus.WAITING)
        );
        assertEquals("inquiry.already.answered", ex.getMessage());
    }
}