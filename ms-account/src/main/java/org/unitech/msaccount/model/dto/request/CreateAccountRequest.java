package org.unitech.msaccount.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.msaccount.model.enums.Currency;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Currency is required")
    private Currency currency;
}
