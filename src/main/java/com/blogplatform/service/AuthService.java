package com.blogplatform.service;

import com.blogplatform.dto.AuthRequestDTO;
import com.blogplatform.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO register(AuthRequestDTO authRequestDTO);

    AuthResponseDTO login(AuthRequestDTO authRequestDTO);
}
