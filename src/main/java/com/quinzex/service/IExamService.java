package com.quinzex.service;

import com.quinzex.dto.AnswerRequest;
import com.quinzex.dto.CreateQuestion;
import com.quinzex.dto.QuestionsResponse;
import com.quinzex.dto.ScoreWithAnswers;
import com.quinzex.entity.Questions;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExamService {
    ;
    public String createQuestion(List<CreateQuestion> createQuestions);
    public ScoreWithAnswers getScore(List<AnswerRequest> answers);
    public String editQuestion(Long id,CreateQuestion createQuestion);
    public String deleteQuestion(Long id);
    public List<?> getRandomQuestionsByCategory(String category, int limit);
    public List<?> getRandomQuestionsByChapterID(Long chapterId, int limit);
    public List<String> getAllExamCategories();
}
