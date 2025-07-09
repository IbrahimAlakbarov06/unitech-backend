package org.unitech.mscurrency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unitech.mscurrency.dto.CurrencyResponse;
import org.unitech.mscurrency.service.CurrencyService;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/rate")
    public CurrencyResponse getExchangeRate(@RequestParam String from, @RequestParam String to) {
        return currencyService.getExchangeRate(from, to);
    }
}
