package com.project.wsms.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor 
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private List<String> roles;
}
