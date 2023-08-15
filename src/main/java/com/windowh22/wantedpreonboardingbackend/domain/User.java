package com.windowh22.wantedpreonboardingbackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Entity
public class User {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String password;
    @Setter
    @Column(length = 100, unique = true)
    private String email;

    protected User() {
    }

    private User(Long id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

}
