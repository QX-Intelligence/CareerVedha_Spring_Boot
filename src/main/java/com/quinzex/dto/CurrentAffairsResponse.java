package com.quinzex.dto;

import com.quinzex.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CurrentAffairsResponse {

    private Long id;
    private String title;
    private String summary;
    private String description;
    private String region;
    private String fileUrl;
    private LocalDateTime createdDate;
    private Language languageCode;
}
