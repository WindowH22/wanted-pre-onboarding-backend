package com.windowh22.wantedpreonboardingbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Setter;


@Builder
@Entity
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private String userId;
    @Setter
    @Column(nullable = false)
    private String password;
    @Setter
    @Column(length = 100, unique = true)
    private String email;

    protected User() {
    }

    private User(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

}
