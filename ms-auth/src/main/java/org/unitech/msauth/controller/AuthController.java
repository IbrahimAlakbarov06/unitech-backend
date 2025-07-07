package org.unitech.msauth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unitech.msauth.model.dto.request.LoginRequest;
import org.unitech.msauth.model.dto.request.RegisterRequest;
import org.unitech.msauth.model.dto.resposne.LoginResponse;
import org.unitech.msauth.model.dto.resposne.UserResponse;
import org.unitech.msauth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authToken) {
        String token = authToken.substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/validate")
    public ResponseEntity<UserResponse> validateToken(@RequestHeader("Authorization") String authToken) {
        String token = authToken.substring(7);
        UserResponse user = authService.validateToken(token);
        return ResponseEntity.ok(user);
    }
}
