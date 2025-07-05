package org.unitech.msuser.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "FIN is required")
    @Size(min = 7, max = 7, message = "FIN must be exactly 7 characters")
    private String fin;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
