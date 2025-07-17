package org.unitech.msbff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unitech.msbff.client.AccountClient;
import org.unitech.msbff.client.TransferClient;
import org.unitech.msbff.exception.NotFoundException;
import org.unitech.msbff.dto.AccountResponse;
import org.unitech.msbff.dto.TransferResponse;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountSummaryService {

    private final AccountClient accountClient;
    private final TransferClient transferClient;

    public AccountResponse getAccountDetails(Long accountId) {
        try {
            return accountClient.getAccountDetails(accountId);
        } catch (Exception e) {
            throw new NotFoundException("Account not found");
        }
    }

    public List<TransferResponse> getAccountTransfers(Long accountId) {
        try {
            return transferClient.getTransfersByAccount(accountId);
        } catch (Exception e) {
            throw new NotFoundException("Transfers not found for account");
        }
    }

    public List<AccountResponse> getUserAccounts(Long userId) {
        try {
            return accountClient.getUserAccounts(userId);
        } catch (Exception e) {
            throw new NotFoundException("Accounts not found for user");
        }
    }
}