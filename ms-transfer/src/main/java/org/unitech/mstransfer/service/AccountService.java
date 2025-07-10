package org.unitech.mstransfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unitech.mstransfer.client.AccountClient;
import org.unitech.mstransfer.model.dto.response.AccountResponse;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountClient accountClient;

    public AccountResponse getAccountById(Long id) {
        return accountClient.getAccountById(id);
    }
}
