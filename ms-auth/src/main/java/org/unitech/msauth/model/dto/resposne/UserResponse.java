package org.unitech.msauth.model.dto.resposne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.msauth.model.enums.Status;
import org.unitech.msauth.model.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String fin;
    private Status status;
    private UserRole role;
}
