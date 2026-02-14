package com.quinzex.service;

import com.quinzex.config.CryptoUtil;
import com.quinzex.dto.CurrentAffairsCreateRequest;
import com.quinzex.dto.CurrentAffairsResponse;
import com.quinzex.entity.CurrentAffairs;
import com.quinzex.enums.Language;
import com.quinzex.repository.CurrentAffairsRepo;
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
public class CurrentAffairsService {

    private final CurrentAffairsRepo currentAffairsRepo;
    private final S3UploadService s3UploadService;
    private final CryptoUtil cryptoUtil;
    private final S3PresignedUrlService s3PresignedUrlService;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String createCurrentAffairs(List<CurrentAffairsCreateRequest> requests) throws IOException {

        List<CurrentAffairs> currentAffairsList = new ArrayList<>();

        for (CurrentAffairsCreateRequest request : requests) {

            String s3Key =
                    s3UploadService.uploadFile(request.getFile(), "current-affairs");

            CurrentAffairs ca = new CurrentAffairs();
            ca.setTitle(request.getTitle());
            ca.setSummary(request.getSummary());
            ca.setDescription(request.getDescription());
            ca.setRegion(request.getRegion());
            ca.setLanguage(request.getLanguage());
            ca.setBucketName(cryptoUtil.encrypt(bucketName));
            ca.setS3Key(cryptoUtil.encrypt(s3Key));

            currentAffairsList.add(ca);
        }

        currentAffairsRepo.saveAll(currentAffairsList);

        return currentAffairsList.size() + " current affairs inserted successfully";
    }

    @Transactional(readOnly = true)
    public List<CurrentAffairsResponse> findAllWithLinks(LocalDateTime cursorTime, Language  language,Long cursorId,int limit) {
        Pageable pageable = PageRequest.of(0, limit);
       List<CurrentAffairs> currentAffairsList;
       if(cursorTime == null && cursorId == null ) {
           currentAffairsList = currentAffairsRepo.findFirstPageByLanguage(language, pageable);
       }else{
           currentAffairsList =currentAffairsRepo.findNextPageByLanguage(
                   language, cursorTime, cursorId, pageable
           );
       }
     return   currentAffairsList  .stream().map(entity->{
                    String bucketName = cryptoUtil.decrypt(entity.getBucketName());
                    String s3Key = cryptoUtil.decrypt(entity.getS3Key());
                    String fileUrl = s3PresignedUrlService.generateViewUrl(bucketName, s3Key);
                    return new CurrentAffairsResponse(entity.getId(),entity.getTitle(),entity.getSummary(),entity.getDescription(),entity.getRegion(),fileUrl,entity.getCreationorupdationDate(),entity.getLanguage());
                }).toList();


    }

    @Transactional
    public String updateCurrentAffairs(Long id , CurrentAffairsCreateRequest currentAffairsCreateRequest) throws IOException {
       try{
           CurrentAffairs ca = currentAffairsRepo.findById(id).orElseThrow(() -> new RuntimeException("Current affair not found"));
           ca.setTitle(currentAffairsCreateRequest.getTitle());
           ca.setSummary(currentAffairsCreateRequest.getSummary());
           ca.setDescription(currentAffairsCreateRequest.getDescription());
           ca.setRegion(currentAffairsCreateRequest.getRegion());
           if(currentAffairsCreateRequest.getFile() != null && !currentAffairsCreateRequest.getFile().isEmpty()) {
               String decryptedKey= cryptoUtil.decrypt(ca.getS3Key());
               s3UploadService.updateFile(decryptedKey,currentAffairsCreateRequest.getFile());
           }
           currentAffairsRepo.save(ca);
           return "Current affair updated successfully";
       }catch (Exception e){
           e.printStackTrace();
             throw e;
       }
    }

    @Transactional
    public String deleteCurrentAffairs(Long id ) throws IOException {
        CurrentAffairs ca = currentAffairsRepo.findById(id).orElseThrow(() -> new RuntimeException("Current affair not found"));
        String s3Key = cryptoUtil.decrypt(ca.getS3Key());
        s3UploadService.deleteFile(s3Key);
        currentAffairsRepo.delete(ca);
        return "Current affair deleted successfully";
    }

    public List<CurrentAffairsResponse> getByRegionWithCursor(String region, Language language ,LocalDateTime cursorTime, Long cursorId, int limit) {
      Pageable pageable = PageRequest.of(0, limit);
        List<CurrentAffairs> currentAffairsList;
      if(cursorTime == null && cursorId == null) {
          currentAffairsList = currentAffairsRepo.findFirstPageByRegionAndLanguage(region,language, pageable);
      }
      else{
          currentAffairsList = currentAffairsRepo.findNextPageByRegionAndLanguage(region, language, cursorTime, cursorId, pageable);
      }

      return currentAffairsList.stream().map(entity->{
          String bucketName = cryptoUtil.decrypt(entity.getBucketName());
          String s3Key = cryptoUtil.decrypt(entity.getS3Key());
          String fileUrl = s3PresignedUrlService.generateViewUrl(bucketName, s3Key);
          return new CurrentAffairsResponse(entity.getId(),entity.getTitle(),entity.getSummary(),entity.getDescription(),entity.getRegion(),fileUrl,entity.getCreationorupdationDate(),entity.getLanguage());
      }).toList();

    }
}
