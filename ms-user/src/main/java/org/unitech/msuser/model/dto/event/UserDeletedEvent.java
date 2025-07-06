package org.unitech.msuser.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeletedEvent {
    private Long userId;
    private String email;
    private LocalDateTime deletedAt;
}