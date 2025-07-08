package org.unitech.msaccount.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePinRequest {
    @NotBlank(message = "Old PIN is required")
    @Pattern(regexp = "\\d{4}", message = "PIN must be 4 digits")
    private String oldPin;

    @NotBlank(message = "New PIN is required")
    @Pattern(regexp = "\\d{4}", message = "PIN must be 4 digits")
    private String newPin;
}
