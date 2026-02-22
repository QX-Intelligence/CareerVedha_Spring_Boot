package com.quinzex.repository;

import com.quinzex.entity.YoutubeUrls;
import com.quinzex.enums.YoutubeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IYoutubeUrls extends JpaRepository<YoutubeUrls, Long> {
    List<YoutubeUrls> findTop10ByCategoryAndIdLessThanOrderByIdDesc(
            YoutubeCategory category,
            Long id
    );

    List<YoutubeUrls> findTop10ByCategoryOrderByIdDesc(
            YoutubeCategory category
    );
}
