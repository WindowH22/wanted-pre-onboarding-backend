package com.windowh22.wantedpreonboardingbackend.repository;

import com.windowh22.wantedpreonboardingbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
