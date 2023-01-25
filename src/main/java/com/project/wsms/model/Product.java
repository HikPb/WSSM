package com.project.wsms.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	@Builder.Default
	private Integer importPrice = 0;
	@Builder.Default
	private Integer sellPrice = 0;
	private ArrayList<String> listCateId;
	private ArrayList<String> listWarehouseId;
	@Builder.Default
	private String supplierId = null;
	@Builder.Default
	private Boolean isSell = true;
	
	

}
