package com.nhnacademy.mart.inquiry.service;

import com.nhnacademy.mart.common.utils.FileUtil;
import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.user.customer.exception.CustomerNotFoundException;
import com.nhnacademy.mart.user.customer.service.CustomerService;
import com.nhnacademy.mart.inquiry.domain.Attachment;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.dto.InquiryRegisterRequest;
import com.nhnacademy.mart.inquiry.exception.InquiryNotFoundException;
import com.nhnacademy.mart.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;

    private final CustomerService customerService;

    @Override
    public Inquiry registerInquiry(InquiryRegisterRequest inquiryRequest, String id) {
        if (!customerService.isExists(id)) {
            throw new CustomerNotFoundException();
        }

        List<Attachment> attachments = inquiryRequest.getAttachments().stream()
                .map(FileUtil::uploadFile)
                .filter(Objects::nonNull)
                .toList();

        log.info(attachments.toString());
        Inquiry inquiry = new Inquiry(inquiryRepository.nextOrderId(),
                inquiryRequest.getTitle(),
                inquiryRequest.getContent(),
                inquiryRequest.getCategory(),
                id,
                attachments);

        return inquiryRepository.save(inquiry);
    }

    @Override
    public List<Inquiry> getAllInquiries(String id, Category category) {
        if (!customerService.isExists(id)) {
            throw new CustomerNotFoundException();
        }

        if (Arrays.asList(Category.values()).contains(category)) {
            return inquiryRepository.findAllByCustomerId(id).stream()
                    .filter(inquiry -> inquiry.getCategory().equals(category))
                    .sorted(Comparator.comparing(Inquiry::getCreateDate).reversed())
                    .toList();
        }

        return inquiryRepository.findAllByCustomerId(id).stream()
                .sorted(Comparator.comparing(Inquiry::getCreateDate).reversed())
                .toList();
    }

    @Override
    public Inquiry getInquiryById(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId);
        if (inquiry == null) {
            throw new InquiryNotFoundException();
        }
        return inquiry;
    }

    @Override
    public List<Inquiry> getNotAnsweredInquiries(Category category) {
        if (Arrays.asList(Category.values()).contains(category)) {
            return inquiryRepository.findNotAnsweredInquiries().stream()
                    .filter(inquiry -> inquiry.getCategory().equals(category))
                    .toList();
        }
        return inquiryRepository.findNotAnsweredInquiries();
    }
}
