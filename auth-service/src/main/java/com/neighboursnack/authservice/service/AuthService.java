package com.neighboursnack.authservice.service;

import com.neighboursnack.common.dto.AuthDTO.LoginRequestDTO;
import com.neighboursnack.common.dto.JwtDTO.JwtResponse;

public interface AuthService {
    JwtResponse signin(LoginRequestDTO loginRequestDTO);
}
