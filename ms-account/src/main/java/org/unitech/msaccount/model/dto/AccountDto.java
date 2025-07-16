package org.unitech.msaccount.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.msaccount.model.enums.AccountStatus;
import org.unitech.msaccount.model.enums.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private Long userId;
    private String cartNumber;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private Currency currency;
    private String pin;
    private String cvv;
}
