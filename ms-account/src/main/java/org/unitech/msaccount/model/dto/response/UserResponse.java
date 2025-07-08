package org.unitech.msaccount.model.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String fin;
    private String status;
    private String role;
}