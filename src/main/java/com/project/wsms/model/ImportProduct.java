package com.project.wsms.collection;

import java.time.LocalDateTime;

import javax.persistence.Table;

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
@Table(name = "import")
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
