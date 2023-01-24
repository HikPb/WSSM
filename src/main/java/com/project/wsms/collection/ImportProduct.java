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
public class ImportProduct {
	
	@Id
	private String importId;
	private String warehouseId;
	private String supplierId;
	private String note;
	private Integer status;
	private LocalDateTime created_at;
	private LocalDateTime expected_at;
	private LocalDateTime updated_at;
}
