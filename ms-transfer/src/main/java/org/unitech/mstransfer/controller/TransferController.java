package org.unitech.mstransfer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unitech.mstransfer.model.dto.request.TransferRequest;
import org.unitech.mstransfer.model.dto.response.TransferResponse;
import org.unitech.mstransfer.service.TransferService;

import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.createTransfer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        TransferResponse response = transferService.getTransferById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransferResponse>> getTransfersByAccount(@PathVariable Long accountId) {
        List<TransferResponse> responses = transferService.getTransfersByAccount(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<TransferResponse>> getRecentTransfersByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<TransferResponse> responses = transferService.getRecentTransfersByUser(userId, limit);
        return ResponseEntity.ok(responses);
    }
}