package com.project.wsms.payload.request;

import com.project.wsms.model.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
