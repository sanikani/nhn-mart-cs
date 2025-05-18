package com.nhnacademy.mart.common.utils;

import com.nhnacademy.mart.common.exception.InternalServerException;
import com.nhnacademy.mart.inquiry.domain.Attachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @BeforeEach
    void setUp() {
        Path tempDir = Paths.get("target", "test-uploads").toAbsolutePath();

        FileUtil.setUploadDir(tempDir + File.separator);
    }

    @Test
    @DisplayName("uploadFile: 빈 파일인 경우 null 반환")
    void uploadFile_empty_returnsNull() {
        MockMultipartFile empty = new MockMultipartFile("file", new byte[0]);
        Attachment result = FileUtil.uploadFile(empty);
        assertNull(result);
    }

    @Test
    @DisplayName("uploadFile: 지원하지 않는 contentType이면 IllegalArgumentException 발생")
    void uploadFile_unsupportedContentType_throws() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes()
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                FileUtil.uploadFile(file)
        );
        assertTrue(ex.getMessage().contains("지원하지 않는 이미지 형식"));
    }

    @Test
    @DisplayName("uploadFile: originalFilename이 null 또는 blank이면 IllegalArgumentException 발생")
    void uploadFile_invalidFilename_throws() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "", "image/png", "data".getBytes()
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                FileUtil.uploadFile(file)
        );
        assertEquals("파일 이름이 유효하지 않습니다.", ex.getMessage());
    }

    @Test
    @DisplayName("uploadFile: multipartFile.transferTo 중 IOException 발생 시 InternalServerException 전환")
    void uploadFile_transferIOException_throwsInternalServer() {
        MultipartFile stub = new MockMultipartFile("file", "pic.png", "image/png", "data".getBytes()) {
            @Override
            public void transferTo(Path dest) throws IOException {
                throw new IOException("disk full");
            }
        };

        InternalServerException ex = assertThrows(InternalServerException.class, () ->
                FileUtil.uploadFile(stub)
        );
        assertEquals("internal.server.error", ex.getMessage());
    }

    @Test
    @DisplayName("uploadFile: 정상 업로드 시 Attachment 반환 및 파일이 생성됨")
    void uploadFile_success() {
        byte[] content = "hello-image".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", content
        );

        Attachment attachment = FileUtil.uploadFile(file);
        assertNotNull(attachment);

        // 필드 검증
        assertEquals("image/jpeg", attachment.getFileType());
        assertEquals("avatar.jpg", attachment.getOriginalFileName());
        assertNotNull(attachment.getFileUrl());

        // 실제 파일 존재 확인
        File saved = new File(attachment.getFileUrl());
        assertTrue(saved.exists());
        assertTrue(saved.length() > 0);

        // cleanup
        saved.delete();
    }
}