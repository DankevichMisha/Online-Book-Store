package com.example.demo.security;

import com.example.demo.dto.user.LoginUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginUserResponseDto authenticate(LoginUserResponseDto userDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
        );
        String generateToken = jwtUtil.generateToken(authentication.getName());
        return new LoginUserResponseDto(generateToken);
    }
}
