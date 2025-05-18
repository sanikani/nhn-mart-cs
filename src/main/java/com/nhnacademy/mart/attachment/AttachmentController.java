package com.nhnacademy.mart.attachment;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping
public class AttachmentController {

    @GetMapping("/attachments/{fileId}")
    public ResponseEntity<Resource> serveAttachment(@PathVariable String fileId) {
        Path filePath = Paths.get("/Users/chosun-nhn23/attachments/", fileId);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            UrlResource resource = new UrlResource(filePath.toUri());
            String encodedFilename = UriUtils.encode(filePath.getFileName().toString(), StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFilename + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
