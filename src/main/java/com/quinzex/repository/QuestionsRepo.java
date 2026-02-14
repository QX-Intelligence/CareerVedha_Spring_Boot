package com.quinzex.repository;

import com.quinzex.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionsRepo extends JpaRepository<Questions,Long> {
    List<Questions> findAllByIdIn(List<Long> ids);

    @Query(
            value = "SELECT * FROM questions WHERE chapter_id = :chapterId ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true
    )
    List<Questions> findRandomByChapterId(
            @Param("chapterId") Long chapterId,
            @Param("limit") int limit
    );
    @Query(
            value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true
    )
    List<Questions> findRandomByCategory(
            @Param("category") String category,
            @Param("limit") int limit
    );
    @Query("""
SELECT DISTINCT q.category 
FROM Questions q 
WHERE q.category IS NOT NULL
""")
    List<String> findDistinctCategories();


}
