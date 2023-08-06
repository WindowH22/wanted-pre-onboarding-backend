package com.windowh22.wantedpreonboardingbackend.repository;

import com.windowh22.wantedpreonboardingbackend.domain.Article;
import com.windowh22.wantedpreonboardingbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndUser(Long articleId, User user);
}
