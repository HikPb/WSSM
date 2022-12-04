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
@Document(collection = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
	
	@Id
	private String productId;
	private String productName;
	private String barcode;
	private Integer importPrice;
	private Integer sellPrice;
	private LocalDateTime created_at;
	private Integer cateId;
	private Boolean isSell;
	
	

}
