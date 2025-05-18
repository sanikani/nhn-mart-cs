package com.nhnacademy.mart.inquiry.service;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.dto.InquiryRegisterRequest;

import java.util.List;

public interface InquiryService {
    Inquiry registerInquiry(InquiryRegisterRequest inquiryRequest, String id);

    List<Inquiry> getAllInquiries(String id, Category category);

    Inquiry getInquiryById(Long inquiryId);

    List<Inquiry> getNotAnsweredInquiries(Category category);
}
