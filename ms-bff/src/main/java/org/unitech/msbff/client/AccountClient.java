package org.unitech.msbff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unitech.msbff.dto.AccountResponse;

import java.util.List;

@FeignClient(name = "ms-account", path = "/accounts")
public interface AccountClient {

    @GetMapping("/user/{userId}")
    List<AccountResponse> getUserAccounts(@PathVariable Long userId);

    @GetMapping("/{id}/details")
    AccountResponse getAccountDetails(@PathVariable Long id);
}