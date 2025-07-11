package org.unitech.msbff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private Long userId;
    private String cartNumber;
    private BigDecimal balance;
    private String accountStatus;
    private String pin;
    private String cvv;
    private LocalDateTime createdAt;
}