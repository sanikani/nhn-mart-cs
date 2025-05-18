package com.nhnacademy.mart.user.admin.controller;

import com.nhnacademy.mart.inquiry.domain.Category;
import com.nhnacademy.mart.inquiry.domain.Content;
import com.nhnacademy.mart.inquiry.domain.Inquiry;
import com.nhnacademy.mart.inquiry.domain.Title;
import com.nhnacademy.mart.inquiry.service.InquiryService;
import com.nhnacademy.mart.user.auth.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminIndexController.class)
class AdminIndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    private MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void setUp() {
        session.setAttribute("admin", "admin");
        session.setAttribute("role", Role.ADMIN);
    }

    @Test
    @DisplayName("GET /cs/admin - 카테고리 미지정 시 전체 미답변 목록")
    void adminIndex_noCategory() throws Exception {
        Inquiry i1 = new Inquiry(1L, new Title("t1"), new Content("c1"), Category.OTHER, "u1", List.of());
        Inquiry i2 = new Inquiry(2L, new Title("t2"), new Content("c2"), Category.COMPLAINTS, "u2", List.of());
        when(inquiryService.getNotAnsweredInquiries(null)).thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/cs/admin").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attribute("inquiries", List.of(i1, i2)));

        verify(inquiryService).getNotAnsweredInquiries(null);
    }

    @Test
    @DisplayName("GET /cs/admin - 카테고리 지정 시 해당 카테고리 필터링")
    void adminIndex_withCategory() throws Exception {
        Inquiry i = new Inquiry(3L, new Title("t3"), new Content("c3"), Category.OTHER, "u3", List.of());
        when(inquiryService.getNotAnsweredInquiries(Category.OTHER)).thenReturn(List.of(i));

        mockMvc.perform(get("/cs/admin")
                        .session(session)
                        .param("category", Category.OTHER.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attribute("inquiries", List.of(i)));

        verify(inquiryService).getNotAnsweredInquiries(Category.OTHER);
    }
}