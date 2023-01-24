package com.project.wsms.collection;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Document(collection = "customers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
	@Id
	private String idCus;
	@Field(value = "Full Name")
	private String nameCus;
	@Field(value = "Phone")
	private String phoneCus;
	private List<Address> cusAddress;
	private Integer npCus; //number of purchases
	private Integer nsoCus; //number of successful orders
	
}
