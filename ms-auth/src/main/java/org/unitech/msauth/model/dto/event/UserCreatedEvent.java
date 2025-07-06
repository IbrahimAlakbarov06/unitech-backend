package org.unitech.msauth.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unitech.msauth.model.enums.Status;
import org.unitech.msauth.model.enums.UserRole;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreatedEvent {
    private Long userId;
    private String fullName;
    private String email;
    private String fin;
    private UserRole role;
    private Status status;
    private LocalDateTime createdAt;
}