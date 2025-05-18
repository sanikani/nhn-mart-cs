package com.nhnacademy.mart.common.utils;

import com.nhnacademy.mart.common.exception.InternalServerException;
import com.nhnacademy.mart.inquiry.domain.Attachment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    private static String uploadDir = "/Users/chosun-nhn23/attachments/";

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/gif", "image/jpeg", "image/png"
    );

    static void setUploadDir(String uploadDir) {
        FileUtil.uploadDir = uploadDir;
    }

    public static Attachment uploadFile(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();

        if (multipartFile.isEmpty()) {
            log.info("파일 없음");
            return null;
        }

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다: " + contentType);
        }

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex != -1) {
                extension = originalFilename.substring(dotIndex);
            }

            String fileId = UUID.randomUUID() + extension;
            Path targetPath = Paths.get(uploadDir, fileId);

            multipartFile.transferTo(targetPath);

            return new Attachment(
                    fileId,
                    targetPath.toAbsolutePath().toString(),
                    contentType,
                    originalFilename
            );
        } catch (IOException e) {
            throw new InternalServerException("internal.server.error");
        }
    }

}

