package com.project.wsms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "users")
public class User {
	@Id
	private String userId;
	private String userEmail;
	private String userName;
	private String userPhone;
	

}
