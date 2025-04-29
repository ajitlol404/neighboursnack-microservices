package com.neighboursnack.authservice.controller;

import com.neighboursnack.authservice.service.AuthService;
import com.neighboursnack.common.dto.AuthDTO.LoginRequestDTO;
import com.neighboursnack.common.dto.JwtDTO.JwtResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.neighboursnack.common.util.AppConstant.BASE_API_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping(BASE_API_PATH + "/signin")
    public ResponseEntity<JwtResponse> signin(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.status(CREATED).body(authService.signin(request));
    }

}
