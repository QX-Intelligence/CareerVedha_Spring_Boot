package com.quinzex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorrectOptionResponse {
    public Long questionId;
    private String correctOption;


}
