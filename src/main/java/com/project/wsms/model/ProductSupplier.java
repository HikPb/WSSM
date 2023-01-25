package com.project.wsms.collection;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "suppliers")
public class ProductSupplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer supplierId;
	
	private List<String> listProductId;
	private List<String> listImportId;
	
	@Column(name = "sup_name", nullable = false)
	private String supplierName;
	@Column(name = "sup_phone", nullable = false)
	private String supplierPhone;
	@Column(name = "sup_name", nullable = false)
	private String suppplierPhone;
	@Column(name = "created_at")
	private LocalDateTime supplierCreatedAt;

}
