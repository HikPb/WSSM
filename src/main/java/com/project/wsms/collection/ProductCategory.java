package com.project.wsms.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "categories")
public class ProductCategory {
	@Id
	private String cateId;
	@Field(value = "Category")
	private String cateName;

}
