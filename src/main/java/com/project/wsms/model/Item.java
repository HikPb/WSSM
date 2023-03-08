package com.project.wsms.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "items")
// @JsonIdentityInfo(
//         generator = ObjectIdGenerators.PropertyGenerator.class,
//         property = "id")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer qty;
	private String sku;
	private Integer purcharsePrice = 0;
	private Integer retailPrice = 0;
	private boolean active = true;
	
	//@JsonIgnore
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ware_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Warehouse warehouse;
	
	//@JsonIgnore
	@JsonManagedReference
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Product product;
 
	//@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy="item")
	private Set<OrderItem> orderItems = new HashSet<>();

	//@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy="item")
	private Set<CqItem> cqItems = new HashSet<>();

	//@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy="item")
	private Set<ImportItem> importItems = new HashSet<>();

	//@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy="item")
	private Set<ExportItem> exportItems = new HashSet<>();

	public void addOrderItem(OrderItem item) {
		this.orderItems.add(item);
	}

	public void addCqItem(CqItem item) {
		this.cqItems.add(item);
	}

	public void addImportItem(ImportItem item) {
		this.importItems.add(item);
	}

	public void addExportItem(ExportItem item) {
		this.exportItems.add(item);
	}
}
