package com.example.demo.service.user;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.book.UserRepository;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ShoppingCartService shoppingCartService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto userDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RegistrationException("User with email "
                    + userDto.getEmail()
                    + " already exists");
        }
        User newUser = userMapper.toModel(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roleUser = roleRepository.findByName(Role.RoleName.ROLE_USER).orElseThrow(() ->
                new EntityNotFoundException("Can't find role " + Role.RoleName.ROLE_USER));
        newUser.setRoles(new HashSet<>(Set.of(roleUser)));
        userRepository.save(newUser);
        shoppingCartService.registerNewShoppingCart(newUser);
        return userMapper.toDto(newUser);
    }
}
