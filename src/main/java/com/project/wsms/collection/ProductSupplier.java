package com.project.wsms.collection;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "suppliers")
public class ProductSupplier {
	@Id
	private String supplierId;
	private List<String> listProductId;
	private List<String> listImportId;
	private String supplierName;
	private String supplierPhone;
	private String supplierAddress;
	private LocalDateTime supplierCreatedAt;

}
