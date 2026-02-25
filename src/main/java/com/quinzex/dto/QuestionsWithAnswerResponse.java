package com.quinzex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsWithAnswerResponse {
    private Long id;
    private String question;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String correctOption;
}
