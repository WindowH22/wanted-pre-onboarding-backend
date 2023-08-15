package com.windowh22.wantedpreonboardingbackend.service;

import com.windowh22.wantedpreonboardingbackend.config.SecurityUtil;
import com.windowh22.wantedpreonboardingbackend.domain.Article;
import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.domain.User;
import com.windowh22.wantedpreonboardingbackend.dto.ArticleRequestDto;
import com.windowh22.wantedpreonboardingbackend.dto.ArticleResponseDto;
import com.windowh22.wantedpreonboardingbackend.repository.ArticleRepository;
import com.windowh22.wantedpreonboardingbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final Response response;

    // 게시글 작성
    public ResponseEntity<Response.Body> saveArticle(ArticleRequestDto dto){
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        Article article = dto.toArticle(user);
        articleRepository.save(article);

        return response.success("게시글 작성이 완료되었습니다.");
    }
    // 게시글 수정
    public ResponseEntity<Response.Body> updateArticle(Long articleId,ArticleRequestDto dto){
        try{
            User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));

            System.out.println(dto.getContent());
            System.out.println(dto.getTitle());


            Article article = articleRepository.findByIdAndUser(articleId,user).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않거나 해당 회원에게 권한이 없습니다."));
            article.setTitle(dto.getTitle());
            article.setContent(dto.getContent());

            articleRepository.save(article);
            return response.success("게시글 수정 완료");
        }catch (EntityNotFoundException e){
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    // 게시글 삭제
    public ResponseEntity<Response.Body> deleteArticle(Long articleId){
        try{
            User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));

            Article article = articleRepository.findByIdAndUser(articleId,user).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않거나 해당 회원에게 권한이 없습니다."));
            articleRepository.delete(article);
            return response.success("게사글 삭제 완료");
        }catch (EntityNotFoundException e){
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> getArticle(Long articleId){
        try {
            Article article = articleRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
            return response.success(ArticleResponseDto.toDto(article));
        }catch (EntityNotFoundException e){
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> getArticles(Pageable pageable){
        Page<Article> articles = articleRepository.findAll(pageable);
        Page<ArticleResponseDto> articleDtoPage = articles.map(m ->
                ArticleResponseDto.toDto(m));
        return response.success(articleDtoPage);
    }


    public void deleteAll(){
        articleRepository.deleteAll();
    }

    public Integer getArticleCount(){
        List<Article> article = articleRepository.findAll();
        return article.size();
    }

}
