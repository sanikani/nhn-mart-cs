package com.nhnacademy.mart.answer.service;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.dto.AnswerRegisterRequest;
import com.nhnacademy.mart.answer.repository.AnswerRepository;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.exception.InquiryNotFoundException;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final InquiryService inquiryService;
    private final AnswerRepository answerRepository;

    @Override
    public Answer registerAnswer(String adminId, AnswerRegisterRequest answerRequest, Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        if (inquiry == null) {
            throw new InquiryNotFoundException();
        }

        Answer answer = answerRepository.save(new Answer(answerRepository.nextOrderId(), answerRequest.getContent(), adminId));
        inquiry.registerAnswer(answer.getAnswerId());
        return answer;
    }

    @Override
    public Answer getAnswerById(Long answerId) {
        return answerRepository.findById(answerId);
    }
}
