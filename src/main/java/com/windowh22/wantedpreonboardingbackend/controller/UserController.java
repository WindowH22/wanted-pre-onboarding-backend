package com.windowh22.wantedpreonboardingbackend.controller;

import com.windowh22.wantedpreonboardingbackend.domain.Response;
import com.windowh22.wantedpreonboardingbackend.dto.UserRequestDto;
import com.windowh22.wantedpreonboardingbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final Response response;


    @PostMapping("/signup")
    public ResponseEntity<Response.Body> signUp(@RequestBody @Valid UserRequestDto.signUpDto dto, Errors errors) {
        try {
            userService.validateHandling(dto);
        } catch (RuntimeException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return userService.signUp(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDto.LoginDto dto) {
        return userService.login(dto);
    }
}
