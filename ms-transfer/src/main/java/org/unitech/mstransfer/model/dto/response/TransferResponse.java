package org.unitech.mstransfer.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.mstransfer.model.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private Long id;
    private Long userId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
    private TransferStatus status;
    private String description;
    private LocalDateTime createdAt;
}