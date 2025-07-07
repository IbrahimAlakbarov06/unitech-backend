package org.unitech.msauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.unitech.msauth.domain.entity.User;
import org.unitech.msauth.domain.repository.UserRepository;
import org.unitech.msauth.exception.UserAlreadyExistsException;
import org.unitech.msauth.exception.UserNotFoundException;
import org.unitech.msauth.mapper.UserMapper;
import org.unitech.msauth.model.dto.request.UserUpdateRequest;
import org.unitech.msauth.model.dto.resposne.UserResponse;
import org.unitech.msauth.model.enums.Status;
import org.unitech.msauth.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
            }
        }

        userMapper.updateEntity(request, user);
        User updatedUser = userRepository.save(user);

        try {
            rabbitTemplate.convertAndSend("user.update",
                    "User updated: " + updatedUser.getEmail() + " with ID: " + updatedUser.getId());
            log.info("Update event sent to RabbitMQ for user: {}", updatedUser.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send update event to RabbitMQ: {}", e.getMessage());
        }

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        try {
            rabbitTemplate.convertAndSend("user.delete",
                    "User deleted: " + user.getEmail() + " with ID: " + user.getId());
            log.info("Delete event sent to RabbitMQ for user: {}", user.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send delete event to RabbitMQ: {}", e.getMessage());
        }
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByFin(String fin) {
        User user = userRepository.findByFin(fin)
                .orElseThrow(() -> new UserNotFoundException("User with fin: " + fin + " not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByStatus(Status status) {
        List<User> users = userRepository.findByStatus(status);
        return userMapper.toResponseList(users);
    }

    @Override
    public Long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }
}
