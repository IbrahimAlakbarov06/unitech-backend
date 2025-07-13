package org.unitech.msbff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.unitech.msbff.dto.TransferResponse;

import java.util.List;

@FeignClient(name = "ms-transfer", path = "/transfers")
public interface TransferClient {

    @GetMapping("/user/{userId}/recent")
    List<TransferResponse> getRecentTransfersByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit
    );

    @GetMapping("/account/{accountId}")
    List<TransferResponse> getTransfersByAccount(@PathVariable Long accountId);
}