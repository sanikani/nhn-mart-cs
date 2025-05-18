package com.nhnacademy.mart.inquiry.repository;

import com.nhnacademy.mart.inquiry.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InquiryRepositoryImplTest {

    private InquiryRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new InquiryRepositoryImpl();
    }

    @Test
    @DisplayName("저장된 답변이 없을 때 nextOrderId()는 1을 반환한다")
    void nextOrderId_WhenEmpty_ShouldReturnOne() {
        assertThat(repository.nextOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("save() 후 nextOrderId()는 기존 최대 ID+1을 반환한다")
    void nextOrderId_AfterSave_ShouldReturnMaxPlusOne() {
        Long id = repository.nextOrderId();
        Inquiry inquiry = getInquiry(id);

        Inquiry savedInquiry = repository.save(inquiry);

        assertThat(savedInquiry).isEqualTo(inquiry);
        assertThat(repository.nextOrderId())
                .isEqualTo(id + 1);
    }

    @Test
    @DisplayName("회원 아이디로 저장된 문의를 찾을 수 있다")
    void getInquiries_ByCustomerId() {
        // given
        Inquiry inquiry1 = getInquiry(repository.nextOrderId());
        repository.save(inquiry1);
        Inquiry inquiry2 = getInquiry(repository.nextOrderId());
        repository.save(inquiry2);

        // when
        List<Inquiry> inquiries = repository.findAllByCustomerId(inquiry1.getCustomerId());
        List<Inquiry> notExists = repository.findAllByCustomerId("notExists");

        // then
        assertThat(inquiries).hasSize(2)
                .extracting("id", "customerId")
                .containsExactlyInAnyOrder(
                        tuple(1L, "user"),
                        tuple(2L, "user")
                );
        assertThat(notExists).isEmpty();
    }

    @Test
    @DisplayName("식별자로 문의 조회")
    void findInquiry_ById() {
        // given
        Long id = repository.nextOrderId();
        Inquiry inquiry = getInquiry(id);
        repository.save(inquiry);

        // when
        Inquiry inquiryById = repository.findById(id);

        // then
        assertThat(inquiryById).isEqualTo(inquiry);
        assertThat(repository.findById(repository.nextOrderId())).isNull();
    }

    @Test
    @DisplayName("답변 대기중인 문의 전체 조회")
    void getInquiries_NotAnswered() {
        // given
        repository.save(getInquiry(repository.nextOrderId()));
        repository.save(getInquiry(repository.nextOrderId()));
        Inquiry inquiry = getInquiry(repository.nextOrderId());
        inquiry.changeInquiryStatus(InquiryStatus.ANSWERED);
        repository.save(inquiry);

        // when
        List<Inquiry> notAnsweredInquiries = repository.findNotAnsweredInquiries();

        // then
        assertThat(notAnsweredInquiries).hasSize(2)
                .extracting("id", "status")
                .containsExactlyInAnyOrder(
                        tuple(1L, InquiryStatus.WAITING),
                        tuple(2L, InquiryStatus.WAITING)
                );
    }

    @Test
    @DisplayName("id로 존재하는지 조회")
    void isExists() {
        // given
        Long id = repository.nextOrderId();
        Inquiry inquiry = getInquiry(id);
        repository.save(inquiry);

        // when
        boolean exists = repository.isExists(id);
        boolean notExists = repository.isExists(2L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    private static Inquiry getInquiry(Long id) {
        return new Inquiry(id,
                new Title("title"),
                new Content("content"),
                Category.OTHER,
                "user",
                new ArrayList<>());
    }
}