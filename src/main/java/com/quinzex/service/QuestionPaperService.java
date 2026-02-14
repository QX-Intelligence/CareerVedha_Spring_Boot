package com.quinzex.service;
import com.quinzex.config.CryptoUtil;
import com.quinzex.dto.CreatePaperDto;
import com.quinzex.dto.PaperResponseDto;
import com.quinzex.entity.PreviousQuestions;
import com.quinzex.enums.Category;
import com.quinzex.repository.QuestionPaperRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionPaperService {


    private final S3UploadService s3UploadService;
    private final S3PresignedUrlService  presignedUrlService;
    private final CryptoUtil cryptoUtil;
    private final QuestionPaperRepo questionPaperRepo;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String createPaper(List<CreatePaperDto> requests) throws IOException {

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Paper list must not be empty");
        }
        List<String> uploadedKeys = new ArrayList<>();
        List<PreviousQuestions> entities = new ArrayList<>();
        try {
            for (CreatePaperDto request : requests) {
                if (request.getFile() == null || request.getFile().isEmpty()) {
                    throw new IllegalArgumentException("Paper file must not be empty");
                }
                String s3Key = s3UploadService.uploadFile(request.getFile(), "prev-papers");
                uploadedKeys.add(s3Key);
                PreviousQuestions pq = new PreviousQuestions();
                pq.setTitle(request.getTitle());
                pq.setDescription(request.getDescription());
                pq.setCategory(request.getCategory());
                pq.setBucketName(cryptoUtil.encrypt(bucketName));
                pq.setS3Key(cryptoUtil.encrypt(s3Key));
                entities.add(pq);
            }
            questionPaperRepo.saveAll(entities);
            return entities.size() + " added successfully";
        } catch (Exception e) {
            for (String Key : uploadedKeys) {
                s3UploadService.deleteFile(Key);
            }
            throw e;
        }
    }


    public List<PaperResponseDto> getPapersByCategory(Category category, LocalDateTime cursor,int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<PreviousQuestions> papers;
        if(cursor==null){
            papers = questionPaperRepo.findByCategoryOrderByCreationDateDesc(category,pageable);
        }
        else{
            papers = questionPaperRepo.findByCategoryWithCursor(category,cursor,pageable);
        }
        return papers.stream().map(p->{
            String decryptedKey = cryptoUtil.decrypt(p.getS3Key());
            String decryptedBucketName = cryptoUtil.decrypt(p.getBucketName());
            String url = presignedUrlService.generateViewUrl(decryptedBucketName,decryptedKey);
            return new PaperResponseDto(p.getQuestionId(),p.getTitle(),p.getDescription(),p.getCategory(),p.getCreationDate(),url);
        }).toList();
    }
    @Transactional
public String deleteMultiplePapers(List<Long> ids) throws IOException {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ID's must not be empty");
        }
        List<PreviousQuestions> papers = questionPaperRepo.findAllById(ids);
        if(papers.size()!=ids.size()) {
            throw new IllegalArgumentException("Some IDs not found");
        }
        for (PreviousQuestions p : papers) {
            String decryptedKey = cryptoUtil.decrypt(p.getS3Key());
            s3UploadService.deleteFile(decryptedKey);
        }
questionPaperRepo.deleteAll(papers);

        return "Deleted successfully";
}


}
