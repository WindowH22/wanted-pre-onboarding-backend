package com.windowh22.wantedpreonboardingbackend.service;

import com.windowh22.wantedpreonboardingbackend.domain.Article;
import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.dto.ArticleRequestDto;
import com.windowh22.wantedpreonboardingbackend.dto.ArticleResponseDto;
import com.windowh22.wantedpreonboardingbackend.dto.UserRequestDto;
import com.windowh22.wantedpreonboardingbackend.repository.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserService userService;

    Article article = null;
    ArticleRequestDto articleRequestDto = null;
    UserRequestDto.signUpDto signUpDto = null;
    UserRequestDto.LoginDto loginDto = null;

    @BeforeEach
    void beforeEach() {
        articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("글 테스트");
        articleRequestDto.setContent("내용 테스트");
    }

    @BeforeEach
    void User() {
        signUpDto = new UserRequestDto.signUpDto("kkk@gmail.com", "abcdef");
        loginDto = new UserRequestDto.LoginDto("kkk@gmail.com", "abcdef");

        userService.signUp(signUpDto);
        userService.login(loginDto);
    }

    @AfterEach
    void afterEach() {
        articleService.deleteAll();
    }

    @Test
    @Transactional
    void saveArticle() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        articleService.saveArticle(articleRequestDto);


        assertEquals(articleService.getArticleCount(), 1);
    }

    @Test
    @Transactional
    void updateArticle() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        articleService.saveArticle(articleRequestDto);
        Article article = articleRepository.findByTitle("글 테스트").get();

        articleRequestDto.setTitle("글 수정");
        articleRequestDto.setContent("내용 수정");
        ResponseEntity<Response.Body> responseEntity = articleService.updateArticle(article.getId(), articleRequestDto);
        ArticleResponseDto data = (ArticleResponseDto) responseEntity.getBody().getData();

        assertEquals(data.getTitle(), articleRequestDto.getTitle());
        assertEquals(data.getContent(), articleRequestDto.getContent());
    }

    @Test
    void deleteArticle() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        articleService.saveArticle(articleRequestDto);
        Article article = articleRepository.findByTitle("글 테스트").get();

        articleService.deleteArticle(article.getId());
        assertEquals(articleService.getArticleCount(), 0);
    }

    @Test
    @Transactional
    void getArticle() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        articleService.saveArticle(articleRequestDto);

        Article article = articleRepository.findByTitle("글 테스트").get();
        ResponseEntity<Response.Body> responseEntity = articleService.getArticle(article.getId());
        ArticleResponseDto data = (ArticleResponseDto) responseEntity.getBody().getData();
        assertEquals(article.getTitle(), data.getTitle());
    }

    @Test
    void getArticles() {
        PageRequest pageable = PageRequest.of(0, 10);
        ResponseEntity<Response.Body> articles = articleService.getArticles(pageable);

        articles.getBody().getData();
        assertEquals(HttpStatus.OK, articles.getStatusCode());
    }


}