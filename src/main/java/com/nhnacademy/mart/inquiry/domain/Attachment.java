package com.nhnacademy.mart.inquiry.domain;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class Attachment {
    String fileName;
    String fileUrl;
    String fileType;
    String originalFileName;
}
