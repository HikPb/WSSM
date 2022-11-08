package com.project.wsms.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "customers")
public class Customer {
	@Id
	private String idCus;
	@Field(value = "Full Name")
	private String nameCus;
	@Field(value = "Phone")
	private String phoneCus;
	@Field(value = "Address")
	private String addressCus;
	@Field(value = "City")
	private String cityCus;
	@Field(value = "District")
	private String districtCus;
	@Field(value = "Wards")
	private String wardsCus;
	private Integer npCus; //number of purchases
	private Integer nsoCus; //number of successful orders
	
}
