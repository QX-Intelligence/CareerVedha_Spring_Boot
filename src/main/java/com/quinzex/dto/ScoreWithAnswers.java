package com.quinzex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ScoreWithAnswers {
    private long score;
    private List<CorrectOptionResponse> correctOptions;
}
