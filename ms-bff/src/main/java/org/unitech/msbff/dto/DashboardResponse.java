package org.unitech.msbff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {
    private UserSummary userSummary;
    private List<AccountSummary> accounts;
    private List<TransferSummary> recentTransfers;
    private List<CurrencyRate> popularRates;
    private BigDecimal totalBalance;
    private Integer totalAccounts;
    private Integer totalTransfers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserSummary {
        private Long id;
        private String fullName;
        private String email;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountSummary {
        private Long id;
        private String cartNumber;
        private BigDecimal balance;
        private String status;
        private String maskedCartNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransferSummary {
        private Long id;
        private Long fromAccountId;
        private Long toAccountId;
        private BigDecimal amount;
        private String status;
        private String description;
        private String createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CurrencyRate {
        private String from;
        private String to;
        private BigDecimal rate;
    }
}