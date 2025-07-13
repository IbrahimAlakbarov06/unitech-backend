package org.unitech.msbff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.unitech.msbff.client.AccountClient;
import org.unitech.msbff.client.AuthClient;
import org.unitech.msbff.client.CurrencyClient;
import org.unitech.msbff.client.TransferClient;
import org.unitech.msbff.exception.ServiceUnavailableException;
import org.unitech.msbff.dto.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AuthClient authClient;
    private final AccountClient accountClient;
    private final TransferClient transferClient;
    private final CurrencyClient currencyClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "dashboard:";
    private static final int CACHE_TTL_MINUTES = 5;

    public DashboardResponse getDashboard(String authToken) {
        try {
            UserResponse user = authClient.validateToken(authToken);

            String cacheKey = CACHE_PREFIX + user.getId();
            DashboardResponse cachedDashboard = (DashboardResponse) redisTemplate.opsForValue().get(cacheKey);

            if (cachedDashboard != null) {
                return cachedDashboard;
            }

            DashboardResponse dashboard = buildDashboard(user);

            redisTemplate.opsForValue().set(cacheKey, dashboard, CACHE_TTL_MINUTES, TimeUnit.MINUTES);

            return dashboard;

        } catch (Exception e) {
            throw new ServiceUnavailableException("Unable to load dashboard data");
        }
    }

    private DashboardResponse buildDashboard(UserResponse user) {
        List<AccountResponse> accounts = accountClient.getUserAccounts(user.getId());

        List<TransferResponse> recentTransfers = transferClient.getRecentTransfersByUser(user.getId(), 5);

        List<DashboardResponse.CurrencyRate> popularRates = getPopularCurrencyRates();

        BigDecimal totalBalance = accounts.stream()
                .map(AccountResponse::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .userSummary(buildUserSummary(user))
                .accounts(buildAccountSummaries(accounts))
                .recentTransfers(buildTransferSummaries(recentTransfers))
                .popularRates(popularRates)
                .totalBalance(totalBalance)
                .totalAccounts(accounts.size())
                .totalTransfers(recentTransfers.size())
                .build();
    }

    private DashboardResponse.UserSummary buildUserSummary(UserResponse user) {
        return DashboardResponse.UserSummary.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }

    private List<DashboardResponse.AccountSummary> buildAccountSummaries(List<AccountResponse> accounts) {
        return accounts.stream()
                .map(account -> DashboardResponse.AccountSummary.builder()
                        .id(account.getId())
                        .cartNumber(account.getCartNumber())
                        .balance(account.getBalance())
                        .status(account.getAccountStatus())
                        .maskedCartNumber(maskCardNumber(account.getCartNumber()))
                        .build())
                .toList();
    }

    private List<DashboardResponse.TransferSummary> buildTransferSummaries(List<TransferResponse> transfers) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return transfers.stream()
                .map(transfer -> DashboardResponse.TransferSummary.builder()
                        .id(transfer.getId())
                        .fromAccountId(transfer.getFromAccountId())
                        .toAccountId(transfer.getToAccountId())
                        .amount(transfer.getAmount())
                        .status(transfer.getStatus())
                        .description(transfer.getDescription())
                        .createdAt(transfer.getCreatedAt().format(formatter))
                        .build())
                .toList();
    }

    private List<DashboardResponse.CurrencyRate> getPopularCurrencyRates() {
        List<DashboardResponse.CurrencyRate> rates = new ArrayList<>();
        String[][] pairs = {{"USD", "AZN"}, {"EUR", "AZN"}, {"TRY", "AZN"}};

        for (String[] pair : pairs) {
            try {
                CurrencyResponse rate = currencyClient.getExchangeRate(pair[0], pair[1]);
                rates.add(DashboardResponse.CurrencyRate.builder()
                        .from(rate.getFrom())
                        .to(rate.getTo())
                        .rate(rate.getRate())
                        .build());
            } catch (Exception e) {
                throw new ServiceUnavailableException("Unable to load rate data");
            }
        }
        return rates;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    }

    public void clearUserCache(Long userId) {
        String cacheKey = CACHE_PREFIX + userId;
        redisTemplate.delete(cacheKey);
    }
}