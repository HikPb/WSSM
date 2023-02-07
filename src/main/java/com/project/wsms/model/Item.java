package com.project.wsms.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer qty;
	private String sku;
	private Integer purcharsePrice;
	private Integer retailPrice;
	private boolean active;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ware_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Warehouse warehouse;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sup_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Supplier supplier;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Product product;
	
	/*
	 * @JsonBackReference public Warehouse getWarehouse() { return this.warehouse; }
	 */
	
	/*
	 * @JsonBackReference public Supplier getSupplier() { return this.supplier; }
	 */
	
	
	@JsonBackReference 
	public Product getProduct() { 
		return this.product; 
	}
	 
}
