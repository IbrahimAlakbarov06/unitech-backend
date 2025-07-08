package org.unitech.msaccount.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unitech.msaccount.model.dto.response.UserResponse;

@FeignClient(name = "ms-auth", path = "/api/users")
public interface UserClient {

    @GetMapping("{id}")
    UserResponse getUserById(@PathVariable Long id);
}
