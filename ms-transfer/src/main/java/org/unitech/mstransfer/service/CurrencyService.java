package org.unitech.mstransfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unitech.mstransfer.client.CurrencyClient;
import org.unitech.mstransfer.model.dto.response.CurrencyResponse;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyClient currencyClient;

    public CurrencyResponse getExchangeRate(String fromCurrency, String toCurrency) {
        return currencyClient.getExchangeRate(fromCurrency, toCurrency);
    }
}
