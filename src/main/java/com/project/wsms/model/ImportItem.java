package com.project.wsms.model;

import lombok.Data;

@Data
public class ImportItem {
	private Product product;
	private Integer quantity;
	private Integer importPrice;
	private Integer sellPrice;
	private Integer current_inventory;
}
