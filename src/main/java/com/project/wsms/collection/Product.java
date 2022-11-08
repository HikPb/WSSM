package com.project.wsms.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Document(collection = "products")
public class Product {
	
	@Id
	private String productId;
	

}
