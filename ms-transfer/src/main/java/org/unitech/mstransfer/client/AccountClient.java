package org.unitech.mstransfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.unitech.mstransfer.model.dto.response.AccountResponse;

import java.math.BigDecimal;

@FeignClient(name = "ms-account", path = "/accounts")
public interface AccountClient {

    @GetMapping("/{id}")
    AccountResponse getAccountById(@PathVariable Long id);

    @PutMapping("/{id}/{amount}")
    AccountResponse updateBalance(@PathVariable Long id, @PathVariable BigDecimal amount);
}