package com.project.wsms.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "warehouse")
public class Warehouse {
	@Id
	private String wareId;
	@Field(value = "Warehouse name")
	private String wareName;
	@Field(value = "Warehouse phone number")
	private String warePhone;
	@Field(value = "Warehouse address")
	private String wareAddress;
	
	private List<String> listImportId;
	
}
