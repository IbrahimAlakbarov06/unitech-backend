package org.unitech.msauth.service;

import org.unitech.msauth.model.dto.request.LoginRequest;
import org.unitech.msauth.model.dto.request.RegisterRequest;
import org.unitech.msauth.model.dto.resposne.LoginResponse;
import org.unitech.msauth.model.dto.resposne.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    UserResponse register(RegisterRequest registerRequest);

    void logout(String token);

    UserResponse validateToken(String token);
}
