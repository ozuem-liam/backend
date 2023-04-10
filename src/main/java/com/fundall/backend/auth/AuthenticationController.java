package com.fundall.backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundall.backend.interceptors.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request) {
        AuthenticationResponse authResponse = authenticationService.register(request);
        ApiResponse<AuthenticationResponse> response = ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Registration successful")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request) {
        try {

            AuthenticationResponse authResponse = authenticationService.login(request);
            ApiResponse<AuthenticationResponse> response = ApiResponse.<AuthenticationResponse>builder()
                    .success(true)
                    .message("Login successful")
                    .data(authResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ApiResponse<AuthenticationResponse> error = ApiResponse.<AuthenticationResponse>builder()
                    .success(false)
                    .message("Incorrect email or password" + ex)
                    .build();

            return ResponseEntity.ok(error);
        }
    }
}
