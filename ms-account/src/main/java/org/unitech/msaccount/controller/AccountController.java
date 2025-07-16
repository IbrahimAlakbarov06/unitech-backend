package org.unitech.msaccount.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unitech.msaccount.model.dto.AccountDto;
import org.unitech.msaccount.model.dto.request.CreateAccountRequest;
import org.unitech.msaccount.model.dto.request.DepositRequest;
import org.unitech.msaccount.model.dto.request.UpdatePinRequest;
import org.unitech.msaccount.model.dto.response.AccountResponse;
import org.unitech.msaccount.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @PathVariable Long userId) {
        List<AccountResponse> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountId}/pin")
    public ResponseEntity<AccountResponse> updatePin(
            @PathVariable Long accountId,
            @Valid @RequestBody UpdatePinRequest request) {
        AccountResponse response = accountService.updatePin(accountId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}/block")
    public ResponseEntity<Void> blockAccount(@PathVariable Long accountId) {
        accountService.blockAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long accountId,
            @RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(accountId, request));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable Long id) {
        AccountDto account = accountService.getAccountDetails(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}/{amount}")
    public ResponseEntity<Void> updateBalance(@PathVariable Long id, @PathVariable BigDecimal amount) {
        accountService.updateBalance(id, amount);
        return ResponseEntity.ok().build();
    }
}