package org.unitech.mscurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.unitech.mscurrency.dto.CurrencyResponse;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RedisTemplate<String, String> redisTemplate;

    public CurrencyResponse getExchangeRate(String from, String to) {
        String cache = "rates:" + from + ":" + to;
        String cachedRate = redisTemplate.opsForValue().get(cache);

        if (cachedRate != null) {
            return CurrencyResponse.builder()
                    .from(from)
                    .to(to)
                    .rate(new BigDecimal(cachedRate))
                    .build();
        }

        BigDecimal rate = getRate(from, to);

        redisTemplate.opsForValue().set(cache, rate.toString(), 5, TimeUnit.MINUTES);

        return CurrencyResponse.builder()
                .from(from)
                .to(to)
                .rate(rate)
                .build();
    }

    private BigDecimal getRate(String from, String to) {
        if ("USD".equalsIgnoreCase(from) && "AZN".equalsIgnoreCase(to)) {
            return new BigDecimal("1.70");
        }
        if ("EUR".equalsIgnoreCase(from) && "AZN".equalsIgnoreCase(to)) {
            return new BigDecimal("1.98");
        }
        if ("AZN".equalsIgnoreCase(from) && "USD".equalsIgnoreCase(to)) {
            return new BigDecimal("0.59");
        }
        if ("AZN".equalsIgnoreCase(from) && "EUR".equalsIgnoreCase(to)) {
            return new BigDecimal("0.51");
        }
        if ("TRY".equalsIgnoreCase(from) && "AZN".equalsIgnoreCase(to)) {
            return new BigDecimal("0.04");
        }
        if ("AZN".equalsIgnoreCase(from) && "TRY".equalsIgnoreCase(to)) {
            return new BigDecimal("23.68");
        }
        if ("TRY".equalsIgnoreCase(from) && "USD".equalsIgnoreCase(to)) {
            return new BigDecimal("0.03");
        }
        if ("USD".equalsIgnoreCase(from) && "TRY".equalsIgnoreCase(to)) {
            return new BigDecimal("40.25");
        }
        return new BigDecimal("1.00");
    }
}
