package com.example.demo.service.user;

import com.example.demo.dto.user.RegisterUserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(RegisterUserRequestDto userDto) throws RegistrationException;
}
