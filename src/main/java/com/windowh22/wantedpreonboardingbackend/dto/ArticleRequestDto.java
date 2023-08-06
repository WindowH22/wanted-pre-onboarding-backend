package com.windowh22.wantedpreonboardingbackend.dto;

import com.windowh22.wantedpreonboardingbackend.domain.Article;
import com.windowh22.wantedpreonboardingbackend.domain.User;
import lombok.Getter;

@Getter
public class ArticleRequestDto {
    private String title;
    private String content;
    @Getter
    public class saveDto{
        private String title;
        private String content;

        public Article toArticle(User user){
            return Article.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }
}
