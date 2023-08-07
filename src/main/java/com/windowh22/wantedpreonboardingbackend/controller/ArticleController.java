package com.windowh22.wantedpreonboardingbackend.controller;

import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.dto.ArticleRequestDto;
import com.windowh22.wantedpreonboardingbackend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Response.Body> getArticle(@RequestParam Long articleId){
        return articleService.getArticle(articleId);
    }

    @GetMapping
    public ResponseEntity<Response.Body> getArticle(Pageable pageable){
        return articleService.getArticles(pageable);
    }

    @PostMapping
    public ResponseEntity<Response.Body> saveArticle(ArticleRequestDto.saveDto saveDto){
        return articleService.saveArticle(saveDto);
    }

    @PutMapping
    public ResponseEntity<Response.Body> updateArticle(@RequestParam Long articleId,ArticleRequestDto updateDto){
        return articleService.updateArticle(articleId,updateDto);
    }

    @DeleteMapping
    public ResponseEntity<Response.Body> deleteArticle(@RequestParam Long articleId){
        return articleService.deleteArticle(articleId);
    }
}
