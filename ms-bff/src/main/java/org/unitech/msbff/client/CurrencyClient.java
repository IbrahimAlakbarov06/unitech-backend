package org.unitech.msbff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unitech.msbff.dto.CurrencyResponse;

@FeignClient(name = "ms-currency", path = "/currency")
public interface CurrencyClient {

    @GetMapping("/rate")
    CurrencyResponse getExchangeRate(@RequestParam String from, @RequestParam String to);
}