package com.nhnacademy.mart.inquiry.repository;

import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.domain.InquiryStatus;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final Map<Long, Inquiry> inquiryMap = new HashMap<>();

    @Override
    public Long nextOrderId() {
        return inquiryMap.keySet()
                .stream()
                .max(Comparator.comparing(Function.identity()))
                .map(l -> l + 1)
                .orElse(1L);
    }

    @Override
    public Inquiry save(Inquiry inquiry) {
        inquiryMap.put(inquiry.getId(), inquiry);
        return inquiry;
    }

    @Override
    public List<Inquiry> findAllByCustomerId(String id) {
        return inquiryMap.values().stream()
                .filter(inquiry -> inquiry.getCustomerId().equals(id))
                .toList();
    }

    @Override
    public boolean isExists(Long inquiryId) {
        return inquiryMap.containsKey(inquiryId);
    }

    @Override
    public Inquiry findById(Long inquiryId) {
        return inquiryMap.get(inquiryId);
    }

    @Override
    public List<Inquiry> findNotAnsweredInquiries() {
        return inquiryMap.values().stream()
                .filter(i -> i.getStatus().equals(InquiryStatus.WAITING))
                .toList();
    }
}
