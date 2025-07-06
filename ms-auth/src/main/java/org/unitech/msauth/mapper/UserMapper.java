package org.unitech.msauth.mapper;

import org.springframework.stereotype.Component;
import org.unitech.msauth.domain.entity.User;
import org.unitech.msauth.model.dto.request.RegisterRequest;
import org.unitech.msauth.model.dto.request.UserUpdateRequest;
import org.unitech.msauth.model.dto.resposne.UserResponse;
import org.unitech.msauth.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setFin(dto.getFin());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    public UserResponse toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setFullName(entity.getFullName());
        response.setEmail(entity.getEmail());
        response.setFin(entity.getFin());
        response.setStatus(entity.getStatus());
        response.setRole(entity.getRole());

        return response;
    }


    public UserResponse toResponse(User user) {
        return toDto(user);
    }

    public void updateEntity(UserUpdateRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        user.setUpdatedAt(LocalDateTime.now());
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
