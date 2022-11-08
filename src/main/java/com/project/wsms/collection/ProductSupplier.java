package com.project.wsms.collection;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "suppliers")
public class ProductSupplier {
	@Id
	private String supplierId;
	private String supplierName;
	private String supplierPhone;
	private String supplierAddress;
	private LocalDateTime supplierCreatedAt;

}
