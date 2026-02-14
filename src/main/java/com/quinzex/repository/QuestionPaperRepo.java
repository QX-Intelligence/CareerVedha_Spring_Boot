package com.quinzex.repository;

import com.quinzex.entity.PreviousQuestions;
import com.quinzex.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionPaperRepo extends JpaRepository<PreviousQuestions, Long> {

    // With cursor
    @Query("""
        SELECT p FROM PreviousQuestions p
        WHERE p.category = :category
        AND p.creationDate < :cursor
        ORDER BY p.creationDate DESC
    """)
    List<PreviousQuestions> findByCategoryWithCursor(
            @Param("category") Category category,
            @Param("cursor") LocalDateTime cursor,
            Pageable pageable
    );

    // First page (no cursor)
    List<PreviousQuestions> findByCategoryOrderByCreationDateDesc(
            Category category,
            Pageable pageable
    );

}
