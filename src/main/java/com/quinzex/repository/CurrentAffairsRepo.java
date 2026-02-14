package com.quinzex.repository;

import com.quinzex.entity.CurrentAffairs;
import com.quinzex.enums.Language;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CurrentAffairsRepo extends JpaRepository<CurrentAffairs, Long> {
    @Query("""
    SELECT c FROM CurrentAffairs c
    WHERE (:language IS NULL OR c.language = :language)
    ORDER BY c.creationorupdationDate DESC, c.id DESC
""")
    List<CurrentAffairs> findFirstPageByLanguage(
            @Param("language") Language language,
            Pageable pageable
    );
    @Query("""
    SELECT c FROM CurrentAffairs c
    WHERE (:language IS NULL OR c.language = :language)
      AND (
            c.creationorupdationDate < :cursorTime
         OR (c.creationorupdationDate = :cursorTime AND c.id < :cursorId)
      )
    ORDER BY c.creationorupdationDate DESC, c.id DESC
""")
    List<CurrentAffairs> findNextPageByLanguage(
            @Param("language") Language language,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );


    @Query("""
    SELECT c FROM CurrentAffairs c
    WHERE (:region IS NULL OR c.region = :region)
      AND (:language IS NULL OR c.language = :language)
      AND (
            c.creationorupdationDate < :cursorTime
            OR (c.creationorupdationDate = :cursorTime AND c.id < :cursorId)
          )
    ORDER BY c.creationorupdationDate DESC, c.id DESC
""")
    List<CurrentAffairs> findNextPageByRegionAndLanguage(
            @Param("region") String region,
            @Param("language") Language language,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );



    @Query("""
    SELECT c FROM CurrentAffairs c
    WHERE (:region IS NULL OR c.region = :region)
      AND (:language IS NULL OR c.language = :language)
    ORDER BY c.creationorupdationDate DESC, c.id DESC
""")
    List<CurrentAffairs> findFirstPageByRegionAndLanguage(
            @Param("region") String region,
            @Param("language") Language language,
            Pageable pageable
    );


}
