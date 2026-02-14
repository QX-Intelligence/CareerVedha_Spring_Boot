package com.quinzex.dto;

import lombok.Data;

@Data
public class AnswerRequest  {
    private Long questionId;
    private String selectedOpt;
}
