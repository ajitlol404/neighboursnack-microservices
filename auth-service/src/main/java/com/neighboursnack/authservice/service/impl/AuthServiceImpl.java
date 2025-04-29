package com.neighboursnack.authservice.service.impl;

import com.neighboursnack.authservice.repository.UserRepository;
import com.neighboursnack.authservice.security.UserDetailsImpl;
import com.neighboursnack.authservice.security.jwt.JwtService;
import com.neighboursnack.authservice.service.AuthService;
import com.neighboursnack.common.dto.AuthDTO;
import com.neighboursnack.common.dto.JwtDTO.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse signin(AuthDTO.LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                );

        authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = userRepository.findByEmail(loginRequestDTO.email())
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(userDetails);

        return new JwtResponse(jwtToken);
    }

}
