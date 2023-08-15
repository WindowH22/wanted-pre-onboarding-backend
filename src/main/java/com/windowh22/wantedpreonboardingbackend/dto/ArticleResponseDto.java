package com.windowh22.wantedpreonboardingbackend.dto;

import com.windowh22.wantedpreonboardingbackend.domain.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ArticleResponseDto {
    private String title;
    private String content;
    private String writer; // 작성자
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime modifiedAt; // 수정일시

    public static ArticleResponseDto toDto(Article article){
        return ArticleResponseDto.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getUser().getEmail())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }
}
