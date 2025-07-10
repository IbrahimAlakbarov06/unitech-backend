package org.unitech.mstransfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unitech.mstransfer.model.dto.response.CurrencyResponse;

@FeignClient(name = "ms-currency", path = "/currency")
public interface CurrencyClient {

    @GetMapping("/rate")
    CurrencyResponse getExchangeRate(@RequestParam String from, @RequestParam String to);
}