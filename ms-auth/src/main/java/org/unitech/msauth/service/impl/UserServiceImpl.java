package org.unitech.msauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unitech.msauth.domain.entity.User;
import org.unitech.msauth.domain.repository.UserRepository;
import org.unitech.msauth.exception.UserAlreadyExistsException;
import org.unitech.msauth.exception.UserNotFoundException;
import org.unitech.msauth.mapper.UserMapper;
import org.unitech.msauth.model.dto.event.UserCreatedEvent;
import org.unitech.msauth.model.dto.event.UserDeletedEvent;
import org.unitech.msauth.model.dto.event.UserUpdatedEvent;
import org.unitech.msauth.model.dto.request.UserCreateRequest;
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
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        if (userRepository.existsByFin(request.getFin())) {
            throw new UserAlreadyExistsException("User with FIN " + request.getFin() + " already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        publishUserCreatedEvent(savedUser);

        return userMapper.toResponse(savedUser);
    }

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

        publishUserUpdatedEvent(updatedUser);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        publishUserDeletedEvent(user);
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

    private void publishUserCreatedEvent(User user) {
        try {
            UserCreatedEvent eventDto = UserCreatedEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .fin(user.getFin())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .createdAt(LocalDateTime.now())
                    .build();

            rabbitTemplate.convertAndSend("user.exchange", "user.created", eventDto);
            log.info("User created event published for user id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish user created event for user id: {}", user.getId(), e);
        }
    }

    private void publishUserUpdatedEvent(User user) {
        try {
            UserUpdatedEvent eventDto = UserUpdatedEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .updatedAt(LocalDateTime.now())
                    .build();

            rabbitTemplate.convertAndSend("user.exchange", "user.updated", eventDto);
            log.info("User updated event published for user id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish user updated event for user id: {}", user.getId(), e);
        }
    }

    private void publishUserDeletedEvent(User user) {
        try {
            UserDeletedEvent eventDto = UserDeletedEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .deletedAt(LocalDateTime.now())
                    .build();

            rabbitTemplate.convertAndSend("user.exchange", "user.deleted", eventDto);
            log.info("User deleted event published for user id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish user deleted event for user id: {}", user.getId(), e);
        }
    }
}
