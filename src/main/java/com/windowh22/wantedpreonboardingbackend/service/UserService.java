package com.windowh22.wantedpreonboardingbackend.service;

import com.windowh22.wantedpreonboardingbackend.config.JwtTokenProvider;
import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.domain.User;
import com.windowh22.wantedpreonboardingbackend.dto.TokenResponse;
import com.windowh22.wantedpreonboardingbackend.dto.UserRequestDto;
import com.windowh22.wantedpreonboardingbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder managerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final Response response;

    // 사용자 회원가입
    public ResponseEntity<Response.Body> signUp(UserRequestDto.signUpDto dto) {
        User user = dto.toUser(passwordEncoder);
        userRepository.save(user);
        return response.success("회원가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    // 사용자 로그인
    public ResponseEntity<Response.Body> login(UserRequestDto.LoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        // LoginDto email, password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.usernamePasswordAuthenticationToken();


        try {
            // 실제 검증 (사용자 비밀번호 체크)
            // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken); // 인증 정보를 기반으로 JWT 토큰 생성
            TokenResponse responseToken = jwtTokenProvider.generateToken(authentication);

            // RefreshToken Redis 저장 (expirationTime 으로 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), responseToken.getRefreshToken(),
                            responseToken.getRefreshTokenExpirationTime(),
                            TimeUnit.MILLISECONDS
                    );


            return response.success(responseToken, "로그인에 성공했습니다.", HttpStatus.OK);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return response.fail("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

    }


    //회원가입시, 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
