package org.unitech.msauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unitech.msauth.domain.entity.User;
import org.unitech.msauth.domain.repository.UserRepository;
import org.unitech.msauth.exception.AuthenticationException;
import org.unitech.msauth.exception.UserAlreadyExistsException;
import org.unitech.msauth.exception.UserNotFoundException;
import org.unitech.msauth.mapper.UserMapper;
import org.unitech.msauth.model.dto.request.LoginRequest;
import org.unitech.msauth.model.dto.request.RegisterRequest;
import org.unitech.msauth.model.dto.resposne.LoginResponse;
import org.unitech.msauth.model.dto.resposne.UserResponse;
import org.unitech.msauth.model.enums.Status;
import org.unitech.msauth.security.JwtService;
import org.unitech.msauth.service.AuthService;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email '" + loginRequest.getEmail() + "' not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new AuthenticationException("User account is not active");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId());

        try {
            rabbitTemplate.convertAndSend("user.login",
                    "User logged in: " + user.getEmail() + " at " + System.currentTimeMillis());
            log.info("Login event sent to RabbitMQ for user: {}", user.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send login event to RabbitMQ: {}", e.getMessage());
        }

        return LoginResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email '" + registerRequest.getEmail() + "' already exists");
        }

        if (userRepository.existsByFin(registerRequest.getFin())) {
            throw new UserAlreadyExistsException("User with fin '" + registerRequest.getFin() + "' already exists");
        }

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saveduser = userRepository.save(user);
        try {
            rabbitTemplate.convertAndSend("user.registration",
                    "User registered: " + saveduser.getEmail() + " with ID: " + saveduser.getId());
            log.info("Registration event sent to RabbitMQ for user: {}", saveduser.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send registration event to RabbitMQ: {}", e.getMessage());
        }

        return userMapper.toResponse(saveduser);
    }

    @Override
    public void logout(String token) {
        try {
            long expiration = jwtService.getTokenExpiration();
            redisTemplate.opsForValue().set("blacklist:" + token, "true", expiration, TimeUnit.MILLISECONDS);

            String username = jwtService.getUsernameFromToken(token);
            rabbitTemplate.convertAndSend("user.logout",
                    "User logged out: " + username + " at " + System.currentTimeMillis());
            log.info("Logout event sent to RabbitMQ for user: {}", username);
        } catch (Exception e) {
            log.warn("Failed to process logout properly: {}", e.getMessage());
        }
    }

    @Override
    public UserResponse validateToken(String token) {

        if (!jwtService.validateToken(token)) {
            throw new AuthenticationException("Invalid token");
        }

        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }
}
