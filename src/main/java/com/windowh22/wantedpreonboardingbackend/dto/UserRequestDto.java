package com.windowh22.wantedpreonboardingbackend.dto;

import com.windowh22.wantedpreonboardingbackend.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class signUpDto {
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "(.*@.*)", message = "@가 포함되어야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = ".{9,}", message = "비밀번호는 8글자 이상이어야 합니다.")
        private String password;

        public User toUser(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }

        public signUpDto(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginDto {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

        public LoginDto(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
