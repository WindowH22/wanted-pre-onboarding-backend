package com.windowh22.wantedpreonboardingbackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Setter;

@Builder
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql과 같은방식 ->AUTO로 설정 시 에러 남
    private Long articleId;

    // setter를 따로 주는 이유는 자동으로 주는 값에 대해 인위적인 변경을 막기위함
    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private User user; // 유저 정보 (ID)
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 내용

    protected Article() {
    }

    private Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
