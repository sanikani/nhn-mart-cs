package com.nhnacademy.mart.inquiry.controller;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Content;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.domain.Title;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryIndexController.class)
class InquiryIndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("GET /cs")
    void getIndexForm() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", "customer");
        Inquiry inquiry1 = new Inquiry(
                1L,
                new Title("제목"),
                new Content("내용"),
                Category.OTHER,
                "user1",
                Collections.emptyList()
        );
        Inquiry inquiry2 = new Inquiry(
                2L,
                new Title("제목"),
                new Content("내용"),
                Category.OTHER,
                "user1",
                Collections.emptyList()
        );
        when(inquiryService.getAllInquiries("customer", null)).thenReturn(List.of(inquiry1, inquiry2));

        // when
        mockMvc.perform(get("/cs").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("inquiries"))
                .andExpect(model().attribute("inquiries", hasSize(2)))
                .andExpect(view().name("index"));

        verify(inquiryService).getAllInquiries("customer", null);
    }
}