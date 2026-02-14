package com.quinzex.dto;

import com.quinzex.enums.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatePaperDto {
    private String title;
    private Category category;
    private String Description;
    private String bucketName;
    private String s3Key;
    private MultipartFile file;
}
