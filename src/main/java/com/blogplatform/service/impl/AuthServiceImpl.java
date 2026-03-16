package com.blogplatform.service.impl;

import com.blogplatform.dto.AuthRequestDTO;
import com.blogplatform.dto.AuthResponseDTO;
import com.blogplatform.entity.User;
import com.blogplatform.exception.ResourceNotFoundException;
import com.blogplatform.repository.UserRepository;
import com.blogplatform.security.JwtTokenProvider;
import com.blogplatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponseDTO register(AuthRequestDTO authRequestDTO) {
        if (userRepository.existsByUsername(authRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(authRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .username(authRequestDTO.getUsername())
                .email(authRequestDTO.getEmail())
                .password(passwordEncoder.encode(authRequestDTO.getPassword()))
                .role(User.Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser.getUsername());

        return AuthResponseDTO.builder()
                .token(token)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(authRequestDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));



       String token = jwtTokenProvider.generateToken(user.getUsername());

        return AuthResponseDTO.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
