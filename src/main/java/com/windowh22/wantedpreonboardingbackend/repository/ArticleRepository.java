package com.windowh22.wantedpreonboardingbackend.repository;

import com.windowh22.wantedpreonboardingbackend.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
