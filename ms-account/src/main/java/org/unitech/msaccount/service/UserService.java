package org.unitech.msaccount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unitech.msaccount.client.UserClient;
import org.unitech.msaccount.model.dto.response.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;

    public UserResponse getUserById(Long id) {
        return userClient.getUserById(id);
    }
}
