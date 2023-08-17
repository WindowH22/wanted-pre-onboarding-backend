package com.windowh22.wantedpreonboardingbackend.service;

import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.dto.TokenResponse;
import com.windowh22.wantedpreonboardingbackend.dto.UserRequestDto;
import com.windowh22.wantedpreonboardingbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    UserRequestDto.signUpDto signUpDto = new UserRequestDto.signUpDto("adck@gmail.com", "password");

    @Test
    void signUp() {
        //given
        //when
        ResponseEntity<Response.Body> responseEntity = userService.signUp(signUpDto);
        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(signUpDto.getEmail(), userRepository.findByEmail(signUpDto.getEmail()).get().getEmail());
    }

    @Test
    void login() {
        //given
        userService.signUp(signUpDto);
        UserRequestDto.LoginDto loginDto = new UserRequestDto.LoginDto(signUpDto.getEmail(), signUpDto.getPassword());

        //when
        ResponseEntity<Response.Body> responseEntity = userService.login(loginDto);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("로그인에 성공했습니다.", responseEntity.getBody().getMessage());
        assertEquals(true, responseEntity.getBody().getData() instanceof TokenResponse);

    }

}