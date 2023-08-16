package com.windowh22.wantedpreonboardingbackend.service;

import com.windowh22.wantedpreonboardingbackend.config.JwtTokenProvider;
import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.domain.User;
import com.windowh22.wantedpreonboardingbackend.dto.TokenResponse;
import com.windowh22.wantedpreonboardingbackend.dto.UserRequestDto;
import com.windowh22.wantedpreonboardingbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
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
        try{
            if(userRepository.findByEmail(dto.getEmail()).isPresent()){
                throw new EntityExistsException("이미 존재하는 회원입니다.");
            }
            User user = dto.toUser(passwordEncoder);
            userRepository.save(user);
            System.out.println("회원가입 완료");
            return response.success("회원가입이 완료되었습니다.", HttpStatus.CREATED);
        }catch (EntityExistsException e){
            return response.fail(e.getMessage(),HttpStatus.CONFLICT);
        }catch (Exception e){
            return response.fail("알 수 없는 문제입니다.", HttpStatus.BAD_REQUEST);
        }


    }

    // 사용자 로그인
    public ResponseEntity<Response.Body> login(UserRequestDto.LoginDto dto) {
        try {

            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

            // LoginDto email, password 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = dto.usernamePasswordAuthenticationToken();

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
        } catch (EntityNotFoundException e){
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    //회원가입시, 유효성 체크
    public ResponseEntity<Response.Body> validateHandling(UserRequestDto.signUpDto dto) {
        System.out.println("유효성 검사 로직 동작");
        System.out.println("이메일 " + dto.getEmail());
        System.out.println("비밀번호 " + dto.getPassword());

            if(!dto.getEmail().contains("@")){
                throw new RuntimeException("@를 포함한 이메일이어야 합니다.");
            }

            if(dto.getPassword().length() < 8){
                throw new RuntimeException("비밀번호는 8글자 이상이어야 합니다.");
            }

        return response.success("검증 통과");

    }

}
