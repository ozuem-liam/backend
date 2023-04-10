package com.fundall.backend.auth;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundall.backend.config.JwtService;
import com.fundall.backend.user.Role;
import com.fundall.backend.user.User;
import com.fundall.backend.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = User.builder()
                                .firstname(request.getFirstname())
                                .lastname(request.getLastname())
                                .phone(request.getPhone())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .build();

                userRepository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .user(user)
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> {
                                        logger.error("User not found with email: {}", request.getEmail());
                                        return new EntityNotFoundException("User could bot be found");
                                });
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .user(user)
                                .token(jwtToken)
                                .build();
        }

}
