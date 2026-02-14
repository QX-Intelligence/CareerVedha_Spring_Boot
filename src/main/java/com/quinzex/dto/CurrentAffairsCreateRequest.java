package com.quinzex.dto;

import com.quinzex.enums.Language;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CurrentAffairsCreateRequest {

    private String title;
    private String summary;
    private String description;
    private String region;
    private MultipartFile file;
    private Language language;
}
