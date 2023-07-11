package com.project.wsms.payload.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdChangeRoleRequest {
    private Set<String> roles;
	private String username;
}
