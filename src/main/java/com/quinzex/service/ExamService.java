package com.quinzex.service;

import com.quinzex.dto.*;
import com.quinzex.entity.Questions;
import com.quinzex.repository.QuestionsRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamService implements IExamService {

    private final QuestionsRepo questionsRepo;
    private final CacheManager cacheManager;
    private static final Logger log = LoggerFactory.getLogger(ExamService.class);

    public ExamService(QuestionsRepo questionsRepo, CacheManager cacheManager) {
        this.questionsRepo = questionsRepo;
        this.cacheManager = cacheManager;
    }


    @Override
    @Transactional
    public String createQuestion(List<CreateQuestion> createQuestions) {

        List<Questions> questionsList = createQuestions.stream().map(question->{
            Questions questions = new Questions();
            questions.setQuestion(question.getQuestion());
            questions.setOpt1(question.getOption1());
            questions.setOpt2(question.getOption2());
            questions.setOpt3(question.getOption3());
            questions.setOpt4(question.getOption4());
            questions.setCorrectOption(question.getCorrectAnswer().toUpperCase());
            questions.setCategory(question.getCategory());
            questions.setChapterId(question.getChapterId());
            return questions;
        }).toList();

        questionsRepo.saveAll(questionsList);
        evictCategoriesCacheSafely();
        return questionsList.size()+" questions added successfully";

    }

    private void evictCategoriesCacheSafely() {
        Cache cache = cacheManager.getCache("categories");
        if (cache == null) {
            return;
        }
        try {
            cache.evict("all");
            // Backward compatibility with entries cached before explicit key was set.
            cache.evict(SimpleKey.EMPTY);
        } catch (RuntimeException ex) {
            log.warn("Categories cache eviction failed; continuing request flow.", ex);
        }
    }

    @Override
    public ScoreWithAnswers getScore(List<AnswerRequest> answers) {

        if (answers == null || answers.isEmpty()) {
            return new ScoreWithAnswers(0, List.of());
        }

        List<Long> questionIds = answers
                .stream()
                .map(AnswerRequest::getQuestionId).distinct()
                .toList();
        Map<Long, String> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        AnswerRequest::getQuestionId,
                        a -> a.getSelectedOpt().toUpperCase()));
        List<Questions> questions = questionsRepo.findAllByIdIn(questionIds);

        long score = questions.stream()
                .filter(q -> answerMap.containsKey(q.getId()) &&
                        q.getCorrectOption().equalsIgnoreCase(
                                answerMap.get(q.getId())))
                .count();
        List<CorrectOptionResponse> correctOptionResponses = questions.stream()
                .map(q -> new CorrectOptionResponse(q.getId(), q.getCorrectOption())).toList();

        return new ScoreWithAnswers(score, correctOptionResponses);
    }

    @Transactional
    @Override
    public String editQuestion(Long id, CreateQuestion createQuestion) {
        Questions questions = questionsRepo.findById(id).orElseThrow(() -> new RuntimeException("Question Not Found"));
        questions.setQuestion(createQuestion.getQuestion());
        questions.setOpt1(createQuestion.getOption1());
        questions.setOpt2(createQuestion.getOption2());
        questions.setOpt3(createQuestion.getOption3());
        questions.setOpt4(createQuestion.getOption4());
        questions.setCorrectOption(createQuestion.getCorrectAnswer().toUpperCase());
        questions.setCategory(createQuestion.getCategory());
        questions.setChapterId(createQuestion.getChapterId());
        questionsRepo.save(questions);
        return "Question Edited Successfully";

    }

    @Transactional
    @Override
    public String deleteQuestion(List<Long> ids) {
      if(ids == null || ids.isEmpty()) {
          throw new RuntimeException("No Questions ids provided");
      }
      List<Questions> existingQuestions = questionsRepo.findAllById(ids);
      if(existingQuestions.isEmpty()) {
          throw new RuntimeException("Questions not found");
      }
      questionsRepo.deleteAll(existingQuestions);
      return  existingQuestions.size()+ " Questions Deleted Successfully";
    }
    @Override
    public List<?> getRandomQuestionsByCategory(String category,int limit) {
        boolean includeAnswers = hasGetAnswersAuthority();
        if(!includeAnswers) {
            return questionsRepo.findRandomByCategory(category,limit).stream().map(this::mapToQuestionResponse).toList();
        }else{
            return questionsRepo.findRandomByCategory(category,limit).stream().map(this::mapToQuestionsWithAnswerResponse).toList();
        }

    }
    @Override
    public List<?> getRandomQuestionsByChapterID(Long chapterId, int limit) {
      boolean includeAnswers = hasGetAnswersAuthority();
      if(!includeAnswers) {
          return questionsRepo.findRandomByChapterId(chapterId,limit).stream().map(this::mapToQuestionResponse).toList();
      }else{
          return questionsRepo.findRandomByChapterId(chapterId,limit).stream().map(this::mapToQuestionsWithAnswerResponse).toList();
      }
    }

    private QuestionsResponse mapToQuestionResponse(Questions questions) {
        return new QuestionsResponse(questions.getId(),questions.getQuestion(),questions.getOpt1(),questions.getOpt2(),questions.getOpt3(),questions.getOpt4(),questions.getCategory(),questions.getChapterId());
    }
    private QuestionsWithAnswerResponse mapToQuestionsWithAnswerResponse(Questions questionsResponse) {
        return new QuestionsWithAnswerResponse(questionsResponse.getId(),
                questionsResponse.getQuestion(),
                questionsResponse.getOpt1(),
                questionsResponse.getOpt2(),
                questionsResponse.getOpt3(),
                questionsResponse.getOpt4(),
                questionsResponse.getCorrectOption(),
                questionsResponse.getCategory(),
                questionsResponse.getChapterId()

        );

    }

    @Override
   @Cacheable(value = "categories", key = "'all'")
    public List<String> getAllExamCategories(){

        return questionsRepo.findDistinctCategories();
    }

    private boolean hasGetAnswersAuthority(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GET_ANSWERS"));
    }
}
