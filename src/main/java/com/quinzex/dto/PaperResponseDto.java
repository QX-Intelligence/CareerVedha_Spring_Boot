package com.quinzex.dto;

import com.quinzex.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaperResponseDto {

    private Long id;
    private String title;
    private String description;
    private Category category;
    private LocalDateTime creationDate;
    private String presignedUrl;
}
