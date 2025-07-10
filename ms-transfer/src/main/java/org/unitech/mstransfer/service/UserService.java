package org.unitech.mstransfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unitech.mstransfer.client.AuthClient;
import org.unitech.mstransfer.model.dto.response.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthClient authClient;

    public UserResponse getUserById(Long id){
        return authClient.getUserById(id);
    }
}
