package com.nhnacademy.mart.inquiry.repository;

import com.nhnacademy.mart.inquiry.domain.Inquiry;

import java.util.List;

public interface InquiryRepository {

    Long nextOrderId();

    Inquiry save(Inquiry inquiry);

    List<Inquiry> findAllByCustomerId(String id);

    boolean isExists(Long inquiryId);

    Inquiry findById(Long inquiryId);

    List<Inquiry> findNotAnsweredInquiries();
}
