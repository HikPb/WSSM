package com.project.wsms.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdChangePwRequest {
    @Size(min=5, max=20, message="Password length must between 5 and 20")
    private String newPassword;
	private String username;
}
