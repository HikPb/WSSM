package com.project.wsms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends AuditModel {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "product_name", nullable = false, length = 50)
	private String productName;
	@Column(name = "barcode", nullable = false, length = 8)
	private String barcode;
	
	@Column(name = "weight", nullable = false, precision = 6, scale = 1)
	private Float weight;
	@Column(name = "total_import", nullable = true)
	private Integer tImport;
	@Column(name = "total_sale", nullable = true)
	private Integer tSale;
	@Column(name = "total_inventory", nullable = true)
	private Integer tInventory;
	
	@OneToMany(mappedBy="product")
	@JsonIgnore
	private Set<Item> items = new HashSet<>();
	
	@OneToMany(mappedBy="product")
	private Set<OrderItem> orderItems = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "product_category", 
		joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false), 
		inverseJoinColumns = @JoinColumn(name = "cate_id", referencedColumnName = "id", nullable = false))
	private Set<Category> categories = new HashSet<>();
	
	
	@JsonManagedReference 
	public Set<Item> getItems(){ 
		return this.items; 
	}
	 
	
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public void addOrderItem(OrderItem item) {
		this.orderItems.add(item);
	}
	
	public void addCategory(Category category) {
		this.categories.add(category);
		category.getProducts().add(this);
	}
	
	public void removeCategory(Integer cateId) {
		Category category = this.categories.stream().filter(c -> Objects.equals(c.getId(), cateId)).findFirst().orElse(null);
		if(category!=null) {
			this.categories.remove(category);
			category.getProducts().remove(this);
		}
	}
	
	public void resetCategory() {
		this.categories.forEach(c->
			c.getProducts().remove(this)
		);
		this.categories.clear();
	}
	
}
