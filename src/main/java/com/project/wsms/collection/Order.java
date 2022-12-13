package com.project.wsms.collection;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

	@Id
	private String orderId;
	private String cusId;
//	private String employeeId;
	private LocalDateTime orderTime;
	private LocalDateTime requireTime;
	
}
