package com.project.wsms.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor 
public class JwtResponse {
    private String token;
    private String type = "Bearer";

}
