package org.unitech.msaccount.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.msaccount.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private Long userId;
    private String cartNumber;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private String pin;
    private String cvv;
    private LocalDateTime createdAt;
}
