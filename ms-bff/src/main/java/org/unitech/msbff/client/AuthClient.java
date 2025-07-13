package org.unitech.msbff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.unitech.msbff.dto.UserResponse;

@FeignClient(name = "ms-auth", path = "/auth")
public interface AuthClient {

    @GetMapping("/validate")
    UserResponse validateToken(@RequestHeader("Authorization") String authToken);
}