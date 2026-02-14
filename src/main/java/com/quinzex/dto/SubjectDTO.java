package com.quinzex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectDTO {
    private Long id;
    private String name;
    private List<ChapterDTO> chapters;
}
