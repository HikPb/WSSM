package com.project.wsms.payload.request;

import com.project.wsms.model.Role;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PwRequest {
    @Size(min=5, max=20, message="Password length must between 5 and 20")
    private String newPassword;
    private Role role;
	private Integer id;
}
