package com.project.wsms.payload.request;

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
public class ChangePwRequest {
    @Size(min=5, max=20, message="Password length must between 5 and 20")
    private String newPassword;

    @Size(min=5, max=20, message="Password length must between 5 and 20")
	private String password;
}
