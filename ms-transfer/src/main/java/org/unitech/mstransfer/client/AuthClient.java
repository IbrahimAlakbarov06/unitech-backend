package org.unitech.mstransfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unitech.mstransfer.model.dto.response.UserResponse;

@FeignClient(name = "ms-auth", path = "/api/users")
public interface AuthClient {

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable Long id);
}