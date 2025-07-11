package org.unitech.msbff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unitech.msbff.dto.TransferResponse;

import java.util.List;

@FeignClient(name = "ms-transfer")
public interface TransferClient {

    @GetMapping("/transfers/account/{accountId}")
    List<TransferResponse> getTransfersByAccount(@PathVariable Long accountId);

    @GetMapping("/transfers/{id}")
    TransferResponse getTransferById(@PathVariable Long id);
}