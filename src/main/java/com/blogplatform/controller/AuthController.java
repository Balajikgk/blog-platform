package com.blogplatform.controller;

import com.blogplatform.dto.AuthRequestDTO;
import com.blogplatform.dto.AuthResponseDTO;
import com.blogplatform.service.AuthService;
import com.blogplatform.Utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO response = authService.register(authRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AuthResponseDTO>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authentication successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO response = authService.login(authRequestDTO);
        return ResponseEntity.ok()
                .body(ApiResponse.<AuthResponseDTO>builder()
                        .success(true)
                        .message("Authentication successful")
                        .data(response)
                        .build());
    }
}
