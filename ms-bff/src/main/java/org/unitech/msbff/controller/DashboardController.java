package org.unitech.msbff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unitech.msbff.dto.AccountResponse;
import org.unitech.msbff.dto.DashboardResponse;
import org.unitech.msbff.dto.TransferResponse;
import org.unitech.msbff.service.AccountSummaryService;
import org.unitech.msbff.service.DashboardService;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final AccountSummaryService accountSummaryService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestHeader("Authorization") String authToken) {

        DashboardResponse dashboard = dashboardService.getDashboard(authToken);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable Long accountId) {
        AccountResponse account = accountSummaryService.getAccountDetails(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/account/{accountId}/transfers")
    public ResponseEntity<List<TransferResponse>> getAccountTransfers(@PathVariable Long accountId) {
        List<TransferResponse> transfers = accountSummaryService.getAccountTransfers(accountId);
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable Long userId) {
        List<AccountResponse> accounts = accountSummaryService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/cache/{userId}")
    public ResponseEntity<String> clearUserCache(@PathVariable Long userId) {
        dashboardService.clearUserCache(userId);
        return ResponseEntity.ok("Cache cleared successfully");
    }
}