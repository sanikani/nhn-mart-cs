package com.nhnacademy.mart.inquiry.service;

import com.nhnacademy.mart.common.utils.FileUtil;
import com.nhnacademy.mart.inquiry.domain.*;
import com.nhnacademy.mart.inquiry.dto.InquiryRegisterRequest;
import com.nhnacademy.mart.inquiry.exception.InquiryNotFoundException;
import com.nhnacademy.mart.inquiry.repository.InquiryRepository;
import com.nhnacademy.mart.user.customer.exception.CustomerNotFoundException;
import com.nhnacademy.mart.user.customer.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceImplTest {

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private InquiryServiceImpl inquiryService;

    private static final String CUSTOMER_ID = "user1";

    @Test
    @DisplayName("등록된 고객이 없으면 CustomerNotFoundException 발생")
    void registerInquiry_NoCustomer_ShouldThrow() {
        InquiryRegisterRequest req = mock(InquiryRegisterRequest.class);
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(false);

        assertThatThrownBy(() -> inquiryService.registerInquiry(req, CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("정상 요청 시 첨부파일 없이 Inquiry 저장 후 반환")
    void registerInquiry_ValidRequest_NoAttachments() {
        InquiryRegisterRequest req = mock(InquiryRegisterRequest.class);
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(true);

        Title title = new Title("제목");
        Content content = new Content("내용");
        when(req.getAttachments()).thenReturn(List.of());
        when(req.getTitle()).thenReturn(title);
        when(req.getContent()).thenReturn(content);
        when(req.getCategory()).thenReturn(Category.OTHER);

        when(inquiryRepository.nextOrderId()).thenReturn(42L);
        Inquiry saved = new Inquiry(42L, title, content, Category.OTHER, CUSTOMER_ID, List.of());
        when(inquiryRepository.save(any(Inquiry.class))).thenReturn(saved);

        Inquiry result = inquiryService.registerInquiry(req, CUSTOMER_ID);

        assertThat(result).isSameAs(saved);
        verify(inquiryRepository).save(argThat(inq ->
                inq.getId() == 42L &&
                        inq.getTitle().equals(title) &&
                        inq.getContent().equals(content) &&
                        inq.getCategory() == Category.OTHER &&
                        CUSTOMER_ID.equals(inq.getCustomerId()) &&
                        inq.getAttachments().isEmpty()
        ));
    }

    @Test
    @DisplayName("첨부파일이 있으면 FileUtil.uploadFile 후 매핑하여 저장")
    void registerInquiry_WithAttachments() {
        MultipartFile dummyFile = new MockMultipartFile("newFile.png", new byte[]{});

        InquiryRegisterRequest req = mock(InquiryRegisterRequest.class);
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(true);
        when(req.getAttachments()).thenReturn(List.of(dummyFile));

        Attachment mapped = new Attachment("newFile.txt", "/path/newFile.txt","type","filename");
        try (MockedStatic<FileUtil> util = Mockito.mockStatic(FileUtil.class)) {
            util.when(() -> FileUtil.uploadFile(dummyFile)).thenReturn(mapped);

            Title title = new Title("t");
            Content content = new Content("c");
            when(req.getTitle()).thenReturn(title);
            when(req.getContent()).thenReturn(content);
            when(req.getCategory()).thenReturn(Category.SUGGESTIONS);

            when(inquiryRepository.nextOrderId()).thenReturn(7L);
            Inquiry saved = new Inquiry(7L, title, content, Category.SUGGESTIONS, CUSTOMER_ID, List.of(mapped));
            when(inquiryRepository.save(any(Inquiry.class))).thenReturn(saved);

            Inquiry result = inquiryService.registerInquiry(req, CUSTOMER_ID);

            assertThat(result.getAttachments()).containsExactly(mapped);
            verify(inquiryRepository).save(any(Inquiry.class));
        }
    }

    @Test
    @DisplayName("getAllInquiries: 고객 없으면 CustomerNotFoundException 발생")
    void getAllInquiries_NoCustomer_ShouldThrow() {
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(false);

        assertThatThrownBy(() -> inquiryService.getAllInquiries(CUSTOMER_ID, Category.OTHER))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("getAllInquiries: 카테고리 필터 및 생성일 내림차순 적용")
    void getAllInquiries_WithCategory_ShouldFilterAndSort() {
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(true);

        Inquiry i1 = new Inquiry(1L, new Title("a"), new Content("a"), Category.OTHER, CUSTOMER_ID, List.of());
        Inquiry i2 = new Inquiry(2L, new Title("b"), new Content("b"), Category.SUGGESTIONS, CUSTOMER_ID, List.of());
        Inquiry i3 = new Inquiry(3L, new Title("c"), new Content("c"), Category.OTHER, CUSTOMER_ID, List.of());

        when(inquiryRepository.findAllByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(i1, i2, i3));

        List<Inquiry> result = inquiryService.getAllInquiries(CUSTOMER_ID, Category.OTHER);

        assertThat(result)
                .hasSize(2)
                .allMatch(inq -> inq.getCategory() == Category.OTHER);
    }

    @Test
    @DisplayName("getAllInquiries: 전체 문의 조회")
    void getAllInquiries_WithoutCategory() {
        // given
        when(customerService.isExists(CUSTOMER_ID)).thenReturn(true);

        Inquiry i1 = new Inquiry(1L, new Title("a"), new Content("a"), Category.OTHER, CUSTOMER_ID, List.of());
        Inquiry i2 = new Inquiry(2L, new Title("b"), new Content("b"), Category.SUGGESTIONS, CUSTOMER_ID, List.of());
        Inquiry i3 = new Inquiry(3L, new Title("c"), new Content("c"), Category.COMPLAINTS, CUSTOMER_ID, List.of());
        when(inquiryRepository.findAllByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(i1, i2, i3));

        // when
        List<Inquiry> result = inquiryService.getAllInquiries(CUSTOMER_ID, null);

        // then
        assertThat(result).hasSize(3)
                .extracting(Inquiry::getId, Inquiry::getCategory)
                .containsExactlyInAnyOrder(
                        tuple(1L, Category.OTHER),
                        tuple(2L, Category.SUGGESTIONS),
                        tuple(3L, Category.COMPLAINTS)
                );
    }

    @Test
    @DisplayName("getInquiryById: 없으면 InquiryNotFoundException 발생")
    void getInquiryById_NotFound_ShouldThrow() {
        when(inquiryRepository.findById(100L)).thenReturn(null);

        assertThatThrownBy(() -> inquiryService.getInquiryById(100L))
                .isInstanceOf(InquiryNotFoundException.class);
    }

    @Test
    @DisplayName("getInquiryById: 존재하면 해당 Inquiry 반환")
    void getInquiryById_Found_ShouldReturn() {
        Inquiry i = new Inquiry(5L, new Title("t"), new Content("c"), Category.OTHER, CUSTOMER_ID, List.of());
        when(inquiryRepository.findById(5L)).thenReturn(i);

        Inquiry result = inquiryService.getInquiryById(5L);

        assertThat(result).isSameAs(i);
    }

    @Test
    @DisplayName("getNotAnsweredInquiries: 카테고리 필터 적용")
    void getNotAnsweredInquiries_WithCategory_ShouldFilter() {
        Inquiry a = new Inquiry(1L, new Title("x"), new Content("x"), Category.SUGGESTIONS, "u", List.of());
        Inquiry b = new Inquiry(2L, new Title("y"), new Content("y"), Category.OTHER, "u", List.of());
        when(inquiryRepository.findNotAnsweredInquiries())
                .thenReturn(List.of(a, b));

        List<Inquiry> result = inquiryService.getNotAnsweredInquiries(Category.OTHER);

        assertThat(result)
                .singleElement()
                .extracting(Inquiry::getCategory)
                .isEqualTo(Category.OTHER);
    }

    @Test
    @DisplayName("getNotAnsweredInquiries: 전체 조회")
    void getNotAnsweredInquiries_WithOutCategory() {
        Inquiry a = new Inquiry(1L, new Title("x"), new Content("x"), Category.SUGGESTIONS, "u", List.of());
        Inquiry b = new Inquiry(2L, new Title("y"), new Content("y"), Category.OTHER, "u", List.of());
        when(inquiryRepository.findNotAnsweredInquiries())
                .thenReturn(List.of(a, b));

        List<Inquiry> result = inquiryService.getNotAnsweredInquiries(null);

        assertThat(result).hasSize(2)
                .extracting(Inquiry::getId, Inquiry::getCategory)
                .containsExactlyInAnyOrder(
                        tuple(1L, Category.SUGGESTIONS),
                        tuple(2L, Category.OTHER)
                );
    }
}
